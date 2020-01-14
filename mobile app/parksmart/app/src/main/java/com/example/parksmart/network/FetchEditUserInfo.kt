package com.example.parksmart.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

//A retrofit Network Interface for the Api
interface EditUserInfoApi{
    @PATCH("editProfile")
    suspend fun editUserInfo(@Header("Authorization") Authorization: String, @Body body: Map<String,String>): Response<Map<String, String>>
}