package com.example.parksmart

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener


public class SlotActivity : AppCompatActivity(), PaymentResultListener {

    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradient_bg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        backGroundColor()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot)

        Log.i("Razorpay", "Slot Activity----------------")
        Checkout.preload(applicationContext)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_reservation, R.id.navigation_payment
            )
        )

        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0.0F
//        this.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.settingsFragment) { // do something here
            findNavController(R.id.nav_host_fragment).navigate(id)
            return true
        }else if(id == android.R.id.home){
            findNavController(R.id.nav_host_fragment).popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        /**
         * Add your logic here for a successful payment response
         */
        Log.i("Razorpay", "$razorpayPaymentID success payment-------------")
    }

    override fun onPaymentError(code: Int, response: String?) {
        /**
         * Add your logic here for a failed payment response
         */
        Log.i("Razorpay", "$response error payment-------------")
    }
}
