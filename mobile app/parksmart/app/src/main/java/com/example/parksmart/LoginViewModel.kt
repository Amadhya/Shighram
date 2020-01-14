package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory.loginApi
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel : ViewModel() {

    val userLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchLogin(email: String, password: String){
        val data = mapOf("email" to email, "password" to password)

        CoroutineScope(Dispatchers.IO).launch {
            val response = loginApi.getLoginDetails(data)

            withContext(Dispatchers.Main) {
                Log.i("Login", "$response")
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        userLiveData.postValue(body)
                    } else {
                        if(response.code() == 400){
                            throw Exception("Invalid email or password")
                        } else{
                            throw Exception(response.message())
                        }
                    }
                } catch (e: HttpException) {
                    errorLiveData.postValue(e.message)
                } catch (e: Throwable) {
                    Log.i("Login", "${e.message}")
                    errorLiveData.postValue(e.message)
                }
            }
        }
    }
}