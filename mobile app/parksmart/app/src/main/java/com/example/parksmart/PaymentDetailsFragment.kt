package com.example.parksmart

import android.R.attr.data
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.parksmart.databinding.FragmentPaymentDetailsBinding
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject


class PaymentDetailsFragment : Fragment() {

    private lateinit var viewModel: PaymentDetailsViewModel

    private lateinit var viewModelFactory: PaymentDetailsViewModelFactory

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentPaymentDetailsBinding>(inflater,
            R.layout.fragment_payment_details,container,false)

        sharedPreference = SharedPreference(requireContext())

        val token = sharedPreference.getValueString("token")

        viewModelFactory = PaymentDetailsViewModelFactory(arguments?.get("rfid").toString(), token.toString())

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PaymentDetailsViewModel::class.java)

        binding.viewMdoel = viewModel
        binding.lifecycleOwner = this

        binding.pay.setOnClickListener{startPayment()}

        return binding.root
    }

    fun startPayment() {
        val checkout = Checkout()
//        checkout.setImage()
        val activity: Activity = requireActivity()

        try {
            viewModel.orderData.observe(this, Observer {
                if (it["status"] == "200"){
                    val options = JSONObject()
                    options.put("name", "Suvidham")
                    options.put("description", "Parking charges")
                    options.put("order_id", it["razorpay_order_id"])
                    options.put("currency", "INR")
                    options.put("amount", it["amount"])
                    options.put("prefill.contact", it["phone"])
                    options.put("prefill.email", it["email"])
                    options.put("prefill.name", it["firstName"]+it["lastName"])
                    checkout.open(activity, options)
                } else {
                    Toast.makeText(requireContext(), it["message"], Toast.LENGTH_SHORT).show()
                }
                viewModel.errorLiveData.observe(this, Observer { it1 ->
                    Toast.makeText(requireContext(), it1, Toast.LENGTH_SHORT).show()
                })
            })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

}
