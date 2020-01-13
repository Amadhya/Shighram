package com.example.parksmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.parksmart.databinding.ActivityLoginBinding
import com.example.parksmart.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var viewModel: SignUpViewModel

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0.0F

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        sharedPreference = SharedPreference(this)

        binding.signUpButton.setOnClickListener{ signup() }

        binding.haveAccount.setOnClickListener{ navLogIn() }
    }

    private fun navLogIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun signup() {

        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val phone = binding.phone.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if(validateSignUp(firstName, lastName, phone, email, password)){
            viewModel.fetchSignUp(firstName, lastName, phone, email, password)

            viewModel.userLiveData.observe(this, Observer {
                if (it["status"] == "200"){
                    it["user_id"]?.let { it1 -> sharedPreference.save("user_id", it1) }
                    it["token"]?.let { it1 -> sharedPreference.save("token", it1) }
                } else {

                }
            })
        }
    }

    private fun validateSignUp(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?,
        password: String?
    ): Boolean {

        if (firstName == null || firstName.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastName == null || lastName.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone == null || phone.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show()
            return false
        }
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
