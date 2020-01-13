package com.example.parksmart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.parksmart.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0.0F

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        sharedPreference = SharedPreference(this)

        Log.i("Logout", "${sharedPreference.checkKey("token")}")

        binding.logInButton.setOnClickListener{ login() }

        binding.createAccount.setOnClickListener{ createAccount() }
    }

    private fun createAccount() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun login() {

        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        Log.i("Login", "$email $password")

        if(validateLogin(email, password)){
            viewModel.fetchLogin(email, password)

            viewModel.userLiveData.observe(this, Observer {
                if (it["status"] == "200"){
                    it["user_id"]?.let { it1 -> sharedPreference.save("user_id", it1) }
                    it["token"]?.let { it1 -> sharedPreference.save("token", it1) }
                    val intent = Intent(this, SlotActivity::class.java)
                    startActivity(intent)
                } else {

                }
            })
        }
    }

    private fun validateLogin(
        email: String?,
        password: String?
    ): Boolean {
        if (email == null || email.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password == null || password.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
