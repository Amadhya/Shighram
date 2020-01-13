package com.example.parksmart.ui.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PaymentViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val paymentLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchPayment(rfid: String, user_id: String){
        val data = mapOf("user_id" to user_id)
        Log.i("Payment", "$data 2.")
        scope.launch {
            val getPropertiesDeferred = Apifactory.paymentOrdersApi.patchPaymentOrder("http://10.0.2.2:8000/api/paymentOrder/${rfid}", data)
            Log.i("Payment", "$getPropertiesDeferred 3.")
            try{
                val response = getPropertiesDeferred.await()
                Log.i("Payment", "$response 4.")
                if(response.isSuccessful){
                    val body = response.body()
                    paymentLiveData.postValue(body)
                } else {
                    if(response.code() == 400){
                        throw Exception("Invalid rfid number")
                    } else{
                        throw Exception(response.message())
                    }
                }
            } catch (e: Exception){
                errorLiveData.postValue(e.message)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
