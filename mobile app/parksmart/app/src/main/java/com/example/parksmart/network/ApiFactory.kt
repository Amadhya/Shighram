package com.example.parksmart.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object Apifactory{
    var logging = HttpLoggingInterceptor()



    //OkhttpClient for building http request url
    private val client = OkHttpClient().newBuilder().addInterceptor(logging.setLevel(
        HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://10.0.2.2:8000/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val loginApi : LoginApi = retrofit().create(LoginApi::class.java)

    val signUpApi: SignUpApi = retrofit().create(SignUpApi::class.java)

    val editUserInfoApi: EditUserInfoApi = retrofit().create(EditUserInfoApi::class.java)

    val verifyRfidApi: VerifyRfidApi = retrofit().create(VerifyRfidApi::class.java)

    val paymentOrdersApi: PaymentOrdersApi = retrofit().create(PaymentOrdersApi::class.java)

    val paymentVerificationApi: PaymentVerificationApi = retrofit().create(PaymentVerificationApi::class.java)

    val userInfoApi: UserInfoApi = retrofit().create(UserInfoApi::class.java)

}