package com.example.parksmart.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

//A retrofit Network Interface for the Api
interface EditUserInfoApi{
    @PATCH("paymentOrder")
    fun patchUserInfo(@Body body: Map<String, String>, rfid: String): Deferred<Response<Map<String, String>>>
}