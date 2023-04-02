package com.nooglers.health.fragments

import android.Manifest
import android.app.DatePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nooglers.health.HCFragment
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentTreatmentBinding
import java.text.SimpleDateFormat
import java.util.*


class TreatmentFragment : HCFragment(R.layout.fragment_treatment) {

    private lateinit var treatmentBinding: FragmentTreatmentBinding
    private lateinit var navController: NavController

    private val locationPermissionCode = 101
    val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        treatmentBinding = FragmentTreatmentBinding.bind(view)

        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fs) as NavHostFragment
        navController = navHostFragment.navController


        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            treatmentBinding.textView8.text = sdf.format(cal.time)

        }

        treatmentBinding.textView8.setOnClickListener {
            DatePickerDialog(view.context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        treatmentBinding.submit.setOnClickListener {
            if(treatmentBinding.etDiseases.text.isNotEmpty()){
                Log.d("debugApp", "diseases")
                val ref = db.collection("treatment").document("users")

                ref.get().addOnSuccessListener { documentSnapshot ->
                    Log.d("debugApp", documentSnapshot.toString())
                    if (documentSnapshot.exists()) {
                        val myArray = documentSnapshot.get("users") as ArrayList<String>
                        if(myArray.contains("akasaudhan02@gmail.com")){
                            navController.popBackStack()
                        }else{
                            myArray.add("akasaudhan02@gmail.com")
                            ref.set(hashMapOf("users" to myArray)).addOnSuccessListener {
                                navController.popBackStack()
                            }
                        }
                    }
                    }

            }else{
                Toast.makeText(view.context, "Fill Diseases Name", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
