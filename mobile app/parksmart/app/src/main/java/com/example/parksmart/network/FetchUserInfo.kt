package com.example.parksmart.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserInfoApi{
    @GET("profile")
    suspend fun getUserInfo(@Header("Authorization") Authorization: String): Response<Map<String,String>>
}