package com.nooglers.health

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattService
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nooglers.health.databinding.ActivityMainBinding
import com.nooglers.health.services.BluetoothService
import java.util.ArrayList


class MainActivity : AppCompatActivity(){

    private lateinit var mainBinding: ActivityMainBinding
    private val locationPermissionCode = 101
    private var previousLocation: Location? = null
    private lateinit var locationManager: LocationManager
    private var bluetoothService : BluetoothService? = null
    private val PERMISSION_REQUEST_CODE = 108
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val TAG = "debugApp"
    private var array = mutableListOf<String>()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val navController = (supportFragmentManager.findFragmentById(mainBinding.navHostFs.id) as NavHostFragment).findNavController()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                mainBinding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                mainBinding.bottomNavigationView.visibility = View.GONE
            }
        }

        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {

            requestPermission()
        } else {
            // permission is already granted
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            initialize()
            val mainHandler = Handler(Looper.getMainLooper())

            mainHandler.post(object : Runnable {
                override fun run() {
                    blServiceInit()
                    Log.d(TAG,"running every 20 second")
                    mainHandler.postDelayed(this, 20000)
                }
            })
        }



        Log.d("debugApp", "MainActivity")
    }

    fun initialize(): Boolean {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.d(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        val ref = db.collection("treatment").document("bl")

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                array = documentSnapshot.get("address") as ArrayList<String>
            }
        }
        blServiceInit()
        return true
    }

    private fun blServiceInit(){
        if (bluetoothAdapter!!.isEnabled) {
            val all_devices = bluetoothAdapter!!.bondedDevices
            for (device in all_devices) {
                val address = device.address
                if(array.contains(address)){
                    Toast.makeText(this, "Alert Red zone $address", Toast.LENGTH_SHORT).show()
                    Log.d("debugApp", "Alert $address")
                }
                Log.d("debugApp", "Found Bluetooth device with address: $address")
            }
        } else {
            Log.d("debugApp", "Bluetooth is not enabled.")
        }
    }

    private val startForBluetoothResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth turned on!", Toast.LENGTH_SHORT).show()
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                initialize()
            } else {
                Log.d("debugApp", "Bluetooth is required!")
                Toast.makeText(this, "Bluetooth is required!", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("debugApp", "${it.key} = ${it.value}")
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startForBluetoothResult.launch(enableBtIntent)
            }
        }

    fun requestPermission(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // only for android 12 and newer versions
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            ))
            Toast.makeText(this, "Android 12", Toast.LENGTH_SHORT).show()
        }else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startForBluetoothResult.launch(enableBtIntent)
            Toast.makeText(this, "Going for bluetooth on", Toast.LENGTH_SHORT).show()
        }
    }


}