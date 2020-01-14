package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
import kotlinx.coroutines.*
import retrofit2.HttpException

class EditViewModel : ViewModel() {

    val editLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchEditDetails(data: Map<String, String>, token: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.editUserInfoApi.editUserInfo("Bearer $token", data)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        editLiveData.postValue(response.body())
                    } else {
                        throw Exception(response.message())
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
