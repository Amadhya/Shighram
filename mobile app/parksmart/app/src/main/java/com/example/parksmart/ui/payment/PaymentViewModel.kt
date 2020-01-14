package com.example.parksmart.ui.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.Event
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.*
import retrofit2.HttpException

class PaymentViewModel : ViewModel() {

    val rfidLiveData = MutableLiveData<Event<Map<String, String>>>()

    var errorLiveData = MutableLiveData<Event<String>>()

    fun fetchPayment(token: String, rfid: String){
        val data = mapOf("rfid" to rfid)
        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.verifyRfidApi.getVerifyRfid("Bearer $token", data)

            withContext(Dispatchers.Main) {
                try {
                    when {
                        response.isSuccessful -> {
                            val body = response.body()
                            if(body != null){
                                rfidLiveData.postValue(Event(body))
                            }
                        }
                        response.code() == 400 -> {
                            errorLiveData.postValue(Event("Invalid rfid"))
                        }
                        else -> {
                            errorLiveData.postValue(Event(response.message()))
                        }
                    }
                } catch (e: HttpException) {
                    val msg = e.message
                    if(msg != null){
                        errorLiveData.postValue(Event(msg))
                    }
                } catch (e: Throwable) {
                    val msg = e.message
                    if(msg != null){
                        errorLiveData.postValue(Event(msg))
                    }
                }
            }
        }
    }

    override fun onCleared() {
        Log.i("wow", "onCLeared*********")
    }
}
