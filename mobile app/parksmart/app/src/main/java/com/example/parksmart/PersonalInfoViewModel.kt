package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.*
import retrofit2.HttpException

class PersonalInfoViewModel: ViewModel() {

    val userInfoLiveData = MutableLiveData<Event<Map<String, String>>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchPersonalInfo(token: String){

        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.userInfoApi.getUserInfo("Bearer $token")

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if(body != null){
                            userInfoLiveData.postValue(Event(body))
                        }
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
