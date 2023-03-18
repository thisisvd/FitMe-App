package com.vdcodeassociate.fitme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.vdcodeassociate.fitme.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            // Force No Night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            // delay LaunchScreen for 6 sec
            object : CountDownTimer(6000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    if (millisUntilFinished / 1000 == 2L) {
                        splashScreenProgressBar.visibility = View.VISIBLE
                    }
                }

                override fun onFinish() {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }.start()

            splashScreenProgressBar.visibility = View.GONE
        }
    }
}