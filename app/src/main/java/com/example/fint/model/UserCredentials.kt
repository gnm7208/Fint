package com.example.fint.model

import com.google.firebase.firestore.PropertyName

data class UserCredentials(
    @get:PropertyName("userId")
    val userId: String = "",

    @get:PropertyName("email")
    val email: String = "",

    @get:PropertyName("name")
    val name: String = "",

    @get:PropertyName("userClass")
    val userClass: String = "",

    @get:PropertyName("profileImageUrl")
    val profileImageUrl: String = ""
)

