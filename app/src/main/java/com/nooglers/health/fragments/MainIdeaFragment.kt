package com.nooglers.health.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nooglers.health.HCFragment
import com.nooglers.health.R
import com.nooglers.health.databinding.FragmentHomeBinding
import com.nooglers.health.databinding.FragmentMainIdeaBinding
import org.w3c.dom.Text
import java.util.*


class MainIdeaFragment : HCFragment(R.layout.fragment_main_idea) {

    private lateinit var mainIdeaBinding: FragmentMainIdeaBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainIdeaBinding = FragmentMainIdeaBinding.bind(view)

        val navHostFragment = (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fs) as NavHostFragment
        navController = navHostFragment.navController


        mainIdeaBinding.view2.setOnClickListener {
            navController.popBackStack()
        }

        mainIdeaBinding.buNext.setOnClickListener {
            if(mainIdeaBinding.radioGroup.checkedRadioButtonId == -1){
                Toast.makeText(view.context, "Please Give Input", Toast.LENGTH_SHORT).show()
            }else if(mainIdeaBinding.radioGroup.checkedRadioButtonId == R.id.radio_button_yes){
                navController.navigate(R.id.action_mainIdeaFragment_to_treatmentFragment)
            }else{

            }
        }

    }
}