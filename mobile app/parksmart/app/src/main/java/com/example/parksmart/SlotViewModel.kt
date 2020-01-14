package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

class SlotViewModel : ViewModel(){

    val paymentVerificationLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchPaymentVerification(token: String, data: Map<String, String>){
        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.paymentVerificationApi.patchPaymentVerification("Bearer $token", data)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        paymentVerificationLiveData.postValue(body)
                    } else {
                        if(response.code() == 400){
                            throw Exception("Payment not successful. Please try again.")
                        } else{
                            throw Exception(response.message())
                        }
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