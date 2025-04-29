package com.example.fint

data class ExamResult(
    val userId: String = "", // This will link the result to a specific user
    val subject: String = "",
    val score: Int = 0,
    val grade: String = ""
)
