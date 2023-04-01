package com.nooglers.health.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nooglers.health.HCFragment
import com.nooglers.health.MainActivity
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentSignUpBinding
import com.nooglers.health.models.UserModel
import com.nooglers.health.utils.SharedPref


class SignUpFragment : HCFragment(R.layout.fragment_sign_up) {

    private lateinit var signUpBinding: FragmentSignUpBinding
    private lateinit var navController: NavController
    val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBinding = FragmentSignUpBinding.bind(view)


        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_oa) as NavHostFragment
        navController = navHostFragment.navController

        signUpBinding.buttonLogin.setOnClickListener {
            if(signUpBinding.edittextEmail.text.isNotEmpty() && signUpBinding.edittextPassword.text.isNotEmpty() && signUpBinding.genderRadioGroup.checkedRadioButtonId != -1 && signUpBinding.edittextName.text.isNotEmpty()){
                val user = UserModel()
                user.name = signUpBinding.edittextName.text.toString()
                if(signUpBinding.genderRadioGroup.checkedRadioButtonId == R.id.maleRadioButton){
                    user.gender = "0"
                }else{
                    user.gender = "1"
                }
                user.email = signUpBinding.edittextEmail.text.toString()
                user.password = signUpBinding.edittextPassword.text.toString()

                db.collection(user.email).document("details").set(user).addOnSuccessListener {
                    Log.d("debugApp", "Stored")
                    //fragment to Activity
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    SharedPref(view.context).logIn(signUpBinding.edittextEmail.text.toString())
                }.addOnFailureListener {
                    Log.d("debugApp", it.message.toString())
                }
            }
        }
    }

}