package com.example.parksmart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class EditViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val editLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchEditDetails(data: Map<String, String>, user_id: String){

//        scope.launch {
//            val getPropertiesDeferred = Apifactory.paymentOrdersApi.patchPaymentOrderAsync(data, user_id)
//
//            try{
//                val response = getPropertiesDeferred.await()
//                if(response.isSuccessful){
//                    val body = response.body()
//                    editLiveData.postValue(body)
//                } else {
//                    if(response.code() == 400){
//                        throw Exception("Error occurred. Please try again.")
//                    } else{
//                        throw Exception(response.message())
//                    }
//                }
//            } catch (e: Exception){
//                errorLiveData.postValue(e.message)
//            }
//        }
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
