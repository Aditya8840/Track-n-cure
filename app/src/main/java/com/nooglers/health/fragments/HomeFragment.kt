package com.nooglers.health.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nooglers.health.HCFragment
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentHomeBinding


class HomeFragment : HCFragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val locationPermissionCode = 101
    private var previousLocation: Location? = null
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fs) as NavHostFragment
        navController = navHostFragment.navController

        homeBinding.imageView.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_mainIdeaFragment)
        }
    }
}