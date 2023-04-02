package com.nooglers.health

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nooglers.health.databinding.ActivityMainBinding
import com.nooglers.health.services.BluetoothService
import com.nooglers.health.utils.SharedPref
import java.util.ArrayList


class MainActivity : AppCompatActivity(), LocationListener {

    // Define the Firestore collection and document references
    private val db = Firebase.firestore
    private val collectionRef = db.collection("user_locations")
    private var documentRef: DocumentReference? = null

    //
    private var TAG = "debugApp"

    // Define the location manager and permission codes
    private val locationPermissionCode = 101
    private lateinit var locationManager: LocationManager

    // Define the previous location and location request interval
    private var previousLocation: Location? = null
    private val locationRequestInterval = 100 // milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the document reference for the current user
        val userId = SharedPref(this).getUser()
        documentRef = collectionRef.document("akasaudhan02@gmail.com")

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode)
        }

        // Get the location manager and request location updates
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            0, 0.1f, this)
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                db.collection("treatment").document("users").get().addOnSuccessListener { documentSnapshot ->
                    Log.d("debugApp", documentSnapshot.toString())
                    if (documentSnapshot.exists()) {
                        val myArray = documentSnapshot.get("users") as ArrayList<String>
                        for(i in myArray){
                            db.collection("user_locations").document(i).get().addOnSuccessListener {
                                val lat = it.data!!["latitude"]
                                val long = it.data!!["longitude"]
                                val distance = distanceInMeters(
                                    lat as Double,
                                    long as Double,
                                    previousLocation!!.latitude,
                                    previousLocation!!.longitude
                                )
                                if(distance<=5){
                                    Toast.makeText(this@MainActivity, "Red Zone Alert", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                Log.d(TAG,"running every 20 second")
                mainHandler.postDelayed(this, 20000)
            }
        })
    }

    override fun onLocationChanged(location: Location) {
        // Check if the location has changed significantly
//        Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show()
        if (previousLocation == null || location.distanceTo(previousLocation!!) >= 0.1) {
            // Store the location in Firestore
            documentRef?.set(mapOf("latitude" to location.latitude, "longitude" to location.longitude))
                ?.addOnSuccessListener {
                    Log.d(TAG, "Location stored successfully")
                }?.addOnFailureListener {
                    Log.e(TAG, "Error storing location: ${it.message}")
                }
            // Update the previous location
            previousLocation = location
        }
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000 // Earth radius in meters
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c // Distance in meters
    }
}
