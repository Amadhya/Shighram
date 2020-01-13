package com.example.parksmart.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

//A retrofit Network Interface for the Api
interface SignUpApi{
    @POST("signup")
    fun getSignUpDetails(@Body body: Map<String, String>): Deferred<Response<Map<String, String>>>
}