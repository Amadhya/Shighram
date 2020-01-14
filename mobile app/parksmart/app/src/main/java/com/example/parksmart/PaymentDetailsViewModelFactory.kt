package com.example.parksmart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentDetailsViewModelFactory(private val rfid: String,private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentDetailsViewModel::class.java)) {
            return PaymentDetailsViewModel(rfid, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}