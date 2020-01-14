package com.example.parksmart.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.parksmart.Event

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

        val token = sharedPreference.getValueString("token")

        if (token != null) {
            viewModel.fetchPayment(token, rfid)
        }

        viewModel.rfidLiveData.observe(this, Observer {
            it.doneEvent { it1 ->
                if (it1["status"] == "200"){
                    if(it1["amount"] == "0"){
                        Toast.makeText(requireContext(), it1["message"], Toast.LENGTH_LONG).show()
                    }else{
                        val actions = PaymentFragmentDirections.actionNavigationPaymentToPaymentDetailsFragment(rfid)
                        NavHostFragment.findNavController(this@PaymentFragment).navigate(actions)
                    }
                }else{
                    Toast.makeText(requireContext(), it1["message"], Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.errorLiveData.observe(this, Observer {
            it.doneEvent { it1 ->
                Toast.makeText(requireContext(), it1, Toast.LENGTH_LONG).show()
            }
        })
    }

}
