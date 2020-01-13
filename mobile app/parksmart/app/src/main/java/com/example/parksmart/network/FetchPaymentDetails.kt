package com.example.parksmart.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Url


//A retrofit Network Interface for the Api
interface PaymentOrdersApi{
    @PATCH("paymentOrder")
    fun patchPaymentOrder( @Url dynamicUrl: String?, @Body body: Map<String,String>): Deferred<Response<Map<String, String>>>
}