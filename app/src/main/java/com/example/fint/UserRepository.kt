package com.example.fint.repository

import com.example.fint.model.UserCredentials
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun saveUserData(userCredentials: UserCredentials) {
        // Saving user data to Firestore
        firestore.collection("users")
            .document(userCredentials.userId)
            .set(userCredentials)
            .await()  // Await the result of saving the user
    }
    suspend fun getUserData(userId: String): UserCredentials? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            document.toObject(UserCredentials::class.java)
        } catch (e: Exception) {
            null  // Handle the error (maybe log it)
        }
    }

    // Additional repository methods if needed, e.g., getUserData
}