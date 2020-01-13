package com.example.parksmart.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.parksmart.LoginViewModel

import com.example.parksmart.R
import com.example.parksmart.SharedPreference
import com.example.parksmart.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentPaymentBinding>(inflater,
            R.layout.fragment_payment,container,false)

        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)

        sharedPreference = SharedPreference(requireContext())

        binding.calPay.setOnClickListener{ calculatePayment(binding.rfid.text.toString())}

        return binding.root
    }

    private fun calculatePayment(rfid: String) {
        val user_id = sharedPreference.getValueString("user_id")

        Log.i("Payment", "$user_id 1.")

        user_id?.let { viewModel.fetchPayment(rfid, it) }

        viewModel.paymentLiveData.observe(this, Observer {
            Log.i("Payment", "$it")
            if (it["status"] == "200"){
                Log.i("Payment", "$it")
            }
        })
//        val actions = PaymentFragmentDirections.actionNavigationPaymentToPaymentDetailsFragment(rfid)
//
//        NavHostFragment.findNavController(this).navigate(actions)
    }

}
