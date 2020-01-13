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

class SignUpViewModel : ViewModel(){
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val userLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchSignUp(firstName: String, lastName: String, phone: String, email: String, password: String){
        val data = mapOf("first_name" to firstName, "last_name" to lastName, "phone" to phone, "email" to email, "password" to password)

        scope.launch {
            val getPropertiesDeferred = Apifactory.signUpApi.getSignUpDetails(data)

            try{
                val response = getPropertiesDeferred.await()
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