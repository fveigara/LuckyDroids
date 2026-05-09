package com.example.luckydroids.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://luckydroids-default-rtdb.firebaseio.com/"

    val api: FirebaseApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(FirebaseApi::class.java)
    }
}