package com.example.parksmart.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

//A retrofit Network Interface for the Api

//object RetrofitFactory {
//    var logging = HttpLoggingInterceptor()
//
//
//
//    //OkhttpClient for building http request url
//    private val client = OkHttpClient().newBuilder().addInterceptor(logging.setLevel(
//        HttpLoggingInterceptor.Level.BODY))
//        .build()
//    const val BASE_URL = "http://10.0.2.2:8000/api/"
//
//    interface UserInfoApi{
//        @POST("api/profile")
//        suspend fun getUserInfo(@Body body: Map<String, String>): Response<Map<String,Map<String, String>>>
//    }
//
//    fun makeRetrofitService(): UserInfoApi {
//        return Retrofit.Builder()
//            .client(client)
//            .baseUrl("http://10.0.2.2:8000/")
//            .addConverterFactory(MoshiConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .build().create(UserInfoApi::class.java)
//    }
//}
    interface UserInfoApi{
        @GET("profile")
        suspend fun getUserInfo(@Header("Authorization") Authorization: String): Response<Map<String,Map<String, String>>>
    }