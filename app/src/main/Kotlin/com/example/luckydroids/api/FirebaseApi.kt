package com.example.luckydroids.api

import com.example.luckydroids.models.Score
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FirebaseApi {

    @GET("scores.json")
    suspend fun getScores(): Response<Map<String, Score>>

    @GET("scores/{uid}.json")
    suspend fun getScore(
        @Path("uid") uid: String
    ): Response<Score>

    @PUT("scores/{uid}.json")
    suspend fun saveScore(
        @Path("uid") uid: String,
        @Body score: Score
    ): Response<Unit>
}
