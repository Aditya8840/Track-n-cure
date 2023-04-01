package com.nooglers.health.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences(
        "com.nooglers.health", Context.MODE_PRIVATE
    )

    fun logIn(email : String) {
        prefs.edit().putInt("loggedIn", 1)
        prefs.edit().putString("email", email)
        prefs.edit().apply()
    }

    fun logout() {
        prefs.edit().putInt("loggedIn", 0)
        prefs.edit().putString("email", "null")
        prefs.edit().apply()
    }

    fun getUser(): String {
        val user = prefs.getString("email", "null")
        return user.toString()
    }



}