package com.example.parksmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.parksmart.databinding.FragmentPaymentHistoryBinding

class PaymentHistoryFragment : Fragment() {

    val l1 = mapOf("location" to "Elante", "amount" to "15", "duration" to "2 hours", "date" to "11th Jan 2020")

    val data = listOf<Map<String, String>>(
        l1,
        l1,
        l1,
        l1,
        l1,
        l1,
        l1,
        l1,
        l1
    )

    private val adapter = PaymentHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentPaymentHistoryBinding>(inflater,
            R.layout.fragment_payment_history,container,false)

        binding.historyList.adapter = adapter

        adapter.data = data

        return binding.root
    }
}
