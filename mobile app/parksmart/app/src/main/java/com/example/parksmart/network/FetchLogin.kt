package com.example.parksmart.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

//A retrofit Network Interface for the Api
interface LoginApi{
    @POST("login")
    fun getLoginDetails(@Body body: Map<String, String>): Deferred<Response<Map<String, String>>>
}