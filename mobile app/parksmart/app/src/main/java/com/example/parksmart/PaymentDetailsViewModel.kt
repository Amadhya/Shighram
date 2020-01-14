package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PaymentDetailsViewModel(rfid: String, token: String) : ViewModel() {

    val amount = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val slot = MutableLiveData<String>()
    val rfid_key = MutableLiveData<String>()

    val orderData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    init {
        fetchPayment(rfid, token)
    }

    fun fetchPayment(rfid: String, token: String){
        val data = mapOf("rfid" to rfid)
        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.paymentOrdersApi.patchPaymentOrder("Bearer $token", data)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        amount.postValue("₹ "+(body!!.getValue("amount").toInt()/100).toString())
                        location.postValue(body["location"])
                        rfid_key.postValue(body["rfid"])
                        slot.postValue(body["slot_number"])
                        date.postValue(body["created_on"])
                        orderData.postValue(body)
                    } else {
                        errorLiveData.postValue(response.message())
                    }
                } catch (e: HttpException) {
                    errorLiveData.postValue(e.message)
                } catch (e: Throwable) {
                    errorLiveData.postValue(e.message)
                }
            }
        }
    }
}