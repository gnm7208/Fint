package com.example.fint

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isSuccess = MutableStateFlow<Boolean?>(null)
    val isSuccess: StateFlow<Boolean?> = _isSuccess

    fun saveContact(name: String, email: String, messageText: String) {
        _isLoading.value = true
        _message.value = null

        val contactData = mapOf(
            "name" to name,
            "email" to email,
            "message" to messageText,
            "timestamp" to FieldValue.serverTimestamp()
        )

        firestore.collection("contacts")
            .add(contactData)
            .addOnSuccessListener {
                _message.value = "Message sent successfully!"
                _isLoading.value = false
                _isSuccess.value = true
            }
            .addOnFailureListener {
                _message.value = "Failed to send message: ${it.message}"
                _isLoading.value = false
                _isSuccess.value = false
            }
    }

}