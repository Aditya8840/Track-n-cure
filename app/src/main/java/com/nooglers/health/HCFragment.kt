package com.nooglers.health

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis

open class HCFragment(layout: Int): Fragment(layout) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.apply {
//            decorView.systemUiVisibility = 8208
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }
    }
}