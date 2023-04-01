package com.nooglers.health.authfragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nooglers.health.HCFragment
import com.nooglers.health.MainActivity
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentSignInBinding
import com.nooglers.health.utils.SharedPref


class SignInFragment : HCFragment(R.layout.fragment_sign_in){


    val db = Firebase.firestore

    private lateinit var signInBinding: FragmentSignInBinding
    private lateinit var navController : NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInBinding = FragmentSignInBinding.bind(view)

        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_oa) as NavHostFragment
        navController = navHostFragment.navController

        signInBinding.signUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        signInBinding.buttonLogin.setOnClickListener {
            if(signInBinding.edittextEmail.text.isNotEmpty() && signInBinding.edittextPassword.text.isNotEmpty()){
                val docRef = db.collection(signInBinding.edittextEmail.text.toString()).document("details")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            if (document.data == null) {
                                Toast.makeText(view.context, "User Not Found", Toast.LENGTH_SHORT)
                                    .show()
                            }else{
                                Log.d("debugApp", "DocumentSnapshot data: ${document.data}")
                                val data = document.data!!.get("password")
                                if(data == signInBinding.edittextPassword.text.toString()){
                                    //fragment to Activity
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    SharedPref(view.context).logIn(signInBinding.edittextEmail.text.toString())
                                }else{
                                    Toast.makeText(view.context, "Wrong credentials", Toast.LENGTH_SHORT).show()
                                }
//                                Log.d("debugApp", data!!.get("password").toString())
                            }
                        } else {
                            Log.d("debugApp", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("debugApp", "get failed with ${exception.message}")
                    }
            }
        }



    }

}