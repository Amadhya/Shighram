package com.example.parksmart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.parksmart.ui.reservation.ReservationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener


class SlotActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var viewModel: SlotViewModel

    private lateinit var sharedPreference: SharedPreference

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

        viewModel = ViewModelProviders.of(this).get(SlotViewModel::class.java)

        sharedPreference = SharedPreference(this)

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

    override fun onPaymentError(code: Int, description: String?, data: PaymentData?) {
        Log.i("Razorpay", "$code ---- $description --- $data")
        Toast.makeText(this, description, Toast.LENGTH_LONG).show()
    }

    override fun onPaymentSuccess(
        razorpayPaymentId: String?,
        paymentData: PaymentData?
    ) {
        val token = sharedPreference.getValueString("token")

        val data = mapOf("razorpay_order_id" to paymentData!!.orderId, "razorpay_payment_id" to paymentData.paymentId, "razorpay_signature" to paymentData.signature)

        if (token != null) {
            viewModel.fetchPaymentVerification(token, data)
        }
        viewModel.paymentVerificationLiveData.observe(this, Observer {
            if(it["status"]=="200"){
                Toast.makeText(this, "Payment Successful.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, SlotActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Payment not Successful. Please try again.", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.errorLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}
