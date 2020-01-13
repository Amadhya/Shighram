package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory.loginApi
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class LoginViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val userLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchLogin(email: String, password: String){
        val data = mapOf("email" to email, "password" to password)

        scope.launch {
            val getPropertiesDeferred = loginApi.getLoginDetails(data)

            Log.i("Login", "$getPropertiesDeferred")

            try{
                val response = getPropertiesDeferred.await()
                Log.i("Login", "$response")
                if(response.isSuccessful){
                    val body = response.body()
                    userLiveData.postValue(body)
                } else {
                    if(response.code() == 400){
                        throw Exception("Invalid username or password")
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