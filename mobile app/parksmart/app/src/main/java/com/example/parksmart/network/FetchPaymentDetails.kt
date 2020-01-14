package com.example.parksmart.network

import retrofit2.Response
import retrofit2.http.*


//A retrofit Network Interface for the Api
interface PaymentOrdersApi{
    @POST("paymentOrder")
    suspend fun patchPaymentOrder(@Header("Authorization") Authorization: String, @Body body: Map<String,String>): Response<Map<String, String>>
}