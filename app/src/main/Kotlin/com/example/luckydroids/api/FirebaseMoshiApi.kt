package com.example.luckydroids.api

import com.example.luckydroids.models.Jackpot
import retrofit2.Response
import retrofit2.http.GET

interface FirebaseMoshiApi {

    @GET("communityPrize.json")
    suspend fun getJackpot(): Response<Jackpot>
}
