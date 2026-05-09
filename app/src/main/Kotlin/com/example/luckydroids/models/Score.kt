package com.example.luckydroids.models

data class Score(
    val uid: String = "",
    val playerName: String = "",
    val points: Int = 0,
    val timestamp: Long = 0
)