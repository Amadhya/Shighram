package com.example.parksmart.network

import retrofit2.Response
import retrofit2.http.*


//A retrofit Network Interface for the Api
interface PaymentVerificationApi{
    @PATCH("paymentVerification")
    suspend fun patchPaymentVerification(@Header("Authorization") Authorization: String, @Body body: Map<String,String>): Response<Map<String, String>>
}