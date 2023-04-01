package com.nooglers.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.nooglers.health.databinding.ActivityOtherBinding
import com.nooglers.health.utils.SharedPref

class AuthActivity : AppCompatActivity() {

    private lateinit var otherBinding: ActivityOtherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherBinding = ActivityOtherBinding.inflate(layoutInflater)
        setContentView(otherBinding.root)

        (supportFragmentManager.findFragmentById(otherBinding.navHostOa.id) as NavHostFragment).findNavController()

    }
}