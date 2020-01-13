package com.example.parksmart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parksmart.network.Apifactory
//import com.example.parksmart.network.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PersonalInfoViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val userInfoLiveData = MutableLiveData<Map<String, String>>()

    var errorLiveData = MutableLiveData<String>()

    fun fetchPersonalInfo(token: String){

        CoroutineScope(Dispatchers.IO).launch {
            val response = Apifactory.userInfoApi.getUserInfo("Bearer $token")
            Log.i("abc", "start =========================")
            withContext(Dispatchers.Main) {
                try {
                    Log.i("abc", "middle *********************")
                    if (response.isSuccessful) {
                        Log.i("abc", "${response.body()} success-------------------------")
                    } else {
                        Log.i("abc", "Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    Log.i("abc", "Exception ${e.message}")
                } catch (e: Throwable) {
                    Log.i("abc","Ooops: Something else went wrong")
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
