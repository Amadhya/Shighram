package com.example.parksmart

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.parksmart.databinding.FragmentPaymentDetailsBinding
import com.razorpay.Checkout
import org.json.JSONObject


class PaymentDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentPaymentDetailsBinding>(inflater,
            R.layout.fragment_payment_details,container,false)

        binding.pay.setOnClickListener{startPayment()}

        return binding.root
    }

    fun startPayment() {
        val checkout = Checkout()
//        checkout.setImage()
        val activity: Activity = requireActivity()

        try {
            val options = JSONObject()
            options.put("name", "Merchant Name")
            options.put("description", "Reference No. #123456")
            options.put("order_id", "order_E2JjTj2fZgd7eJ")
            options.put("currency", "INR")
            options.put("amount", "1500")
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.i("Razorpay", "Error in starting Razorpay Checkout", e)
        }
    }

}
