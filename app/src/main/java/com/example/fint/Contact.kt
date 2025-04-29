package com.example.fint

data class Contact(
    val name: String = "",
    val email: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
