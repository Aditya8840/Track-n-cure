package com.nooglers.health.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nooglers.health.HCFragment
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentFeelillBinding


class FeelillFragment : HCFragment(R.layout.fragment_feelill) {

    private lateinit var feelillBinding: FragmentFeelillBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feelillBinding = FragmentFeelillBinding.bind(view)
        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fs) as NavHostFragment
        navController = navHostFragment.navController


    }

}