package com.example.luckydroids.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiClient {

    private const val BASE_URL =
        "https://luckydroidsonline-default-rtdb.europe-west1.firebasedatabase.app/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: FirebaseMoshiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(FirebaseMoshiApi::class.java)
    }
}
