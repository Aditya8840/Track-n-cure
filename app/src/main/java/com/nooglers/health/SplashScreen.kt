package com.nooglers.health

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.nooglers.health.databinding.ActivitySplashScreenBinding
import com.nooglers.health.utils.SharedPref

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                TODO("Not yet implemented")
            }

            override fun onFinish() {
                if(SharedPref(view.context).getUser() == "null"){
                    val intent = Intent(applicationContext, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}