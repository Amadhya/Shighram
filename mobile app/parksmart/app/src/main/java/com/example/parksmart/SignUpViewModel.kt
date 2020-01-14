package com.example.parksmart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

class SignUpViewModel : ViewModel(){

    val userLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchSignUp(firstName: String, lastName: String, phone: String, email: String, password: String){
        val data = mapOf("first_name" to firstName, "last_name" to lastName, "phone" to phone, "email" to email, "password" to password)

        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.signUpApi.getSignUpDetails(data)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        userLiveData.postValue(body)
                    } else {
                        if(response.code() == 400){
                            throw Exception("Email already in use")
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