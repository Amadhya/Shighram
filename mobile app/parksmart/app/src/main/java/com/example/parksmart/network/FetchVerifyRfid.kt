package com.example.parksmart.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface VerifyRfidApi{
    @PATCH("verify_rfid")
    suspend fun getVerifyRfid(@Header("Authorization") Authorization: String, @Body body: Map<String, String>): Response<Map<String, String>>
}