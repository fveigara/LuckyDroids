package com.example.luckydroids.api

import com.example.luckydroids.models.Score
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FirebaseApi {

    @GET("scores.json")
    suspend fun getScores(): Map<String, Score>

    @POST("scores.json")
    suspend fun saveScore(
        @Body score: Score
    ): Response<Unit>
}