package com.example.parksmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat

class SplashScreenActivity : AppCompatActivity() {

    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradient_bg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        backGroundColor()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val TIME_OUT = 2000
        val homeIntent = Intent(this, MainActivity::class.java)
        Handler().postDelayed({
            //Do some stuff here, like implement deep linking
            startActivity(homeIntent)
            finish()
        }, TIME_OUT.toLong())
    }
}
