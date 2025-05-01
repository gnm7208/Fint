package com.example.fint.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fint.ExamResult
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@HiltViewModel
class ExamViewModel @Inject constructor(
    private val firestore: FirebaseFirestore // Firestore instance
) : ViewModel() {

    private val _examResults = MutableStateFlow<List<ExamResult>>(emptyList())
    val examResults: StateFlow<List<ExamResult>> = _examResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch exam results for the logged-in user
    // In ExamViewModel
    fun fetchExamResults(userId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = firestore.collection("examResults")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val results = result.documents.mapNotNull { document ->
                    document.toObject(ExamResult::class.java)
                }
                _examResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
