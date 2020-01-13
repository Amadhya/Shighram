package com.example.parksmart

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = SharedPreference(this)

        val logInActivityIntent = Intent(this, LoginActivity::class.java)

        val slotActivityIntent = Intent(this, SlotActivity::class.java)

        if(sharedPreference.checkKey("token")){
            startActivity(slotActivityIntent)
        }else{
            startActivity(logInActivityIntent)
        }

        finish()
    }
}
