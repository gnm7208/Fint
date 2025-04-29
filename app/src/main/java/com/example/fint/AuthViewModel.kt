package com.example.fint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fint.model.UserCredentials
import com.example.fint.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.content.Context
import android.net.Uri
import android.widget.Toast


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    // Email and name state
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _userClass = MutableStateFlow("")
    val userClass: StateFlow<String> = _userClass

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    private val _userData = MutableStateFlow<UserCredentials?>(null)
    val userData: StateFlow<UserCredentials?> = _userData

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _passwordResetState = MutableStateFlow<AuthState>(AuthState.Idle)
    val passwordResetState: StateFlow<AuthState> = _passwordResetState

    val isLoading: Boolean
        get() = _loginState.value == AuthState.Loading || _registerState.value == AuthState.Loading

    val errorMessage: String?
        get() = when {
            _loginState.value is AuthState.Error -> (_loginState.value as AuthState.Error).message
            _registerState.value is AuthState.Error -> (_registerState.value as AuthState.Error).message
            else -> null
        }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _isLoggedIn.value = user != null

            if (user != null) {
                loadUserData(user.uid)
            } else {
                _userData.value = null
            }
        }
    }

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    loadUserData(uid) // Ensure immediate user data loading
                }
                _loginState.value = AuthState.Success
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    val userData = UserCredentials(
                        userId = user.uid,
                        email = email,
                        name = name,
                        userClass = userClass.value,
                        profileImageUrl = ""
                    )

                    saveUserData(userData)
                    _registerState.value = AuthState.Success
                } else {
                    _registerState.value = AuthState.Error("User creation failed.")
                }

            } catch (e: Exception) {
                _registerState.value = AuthState.Error(e.message ?: "Registration failed.")
            }
        }
    }

    private fun saveUserData(userData: UserCredentials) {
        firestore.collection("users")
            .document(userData.userId)
            .set(userData)
            .addOnSuccessListener {
                _userData.value = userData
            }
            .addOnFailureListener { e ->
                _registerState.value = AuthState.Error(e.message ?: "Failed to save user data")
            }
    }

    private fun loadUserData(userId: String) {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                document?.toObject(UserCredentials::class.java)?.let {
                    _userData.value = it
                }
            }
            .addOnFailureListener {
                // Optional: log or show a toast/snackbar
            }
    }


    fun fetchUserData() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid
            uid?.let { loadUserData(it) }
        }
    }

    fun logout() {
        auth.signOut()
        _userData.value = null
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }

    fun resetAuthState() {
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
        _passwordResetState.value = AuthState.Idle
    }

    // Function to send password reset email
    fun sendPasswordResetEmail(email: String) {
        _passwordResetState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()  // Firebase reset email function
                _passwordResetState.value = AuthState.Success
            } catch (e: Exception) {
                _passwordResetState.value = AuthState.Error(e.message ?: "Password reset failed")
            }
        }
    }

    fun uploadProfileImage(uri: Uri, context: Context) {
        val userId = auth.currentUser?.uid ?: return

        val storageRef = FirebaseStorage.getInstance().reference
            .child("profileImages/$userId.jpg")

        viewModelScope.launch {
            try {
                // Upload to Firebase Storage
                val uploadTaskSnapshot = storageRef.putFile(uri).await()

                // ✅ Check if upload was successful
                if (uploadTaskSnapshot.metadata != null) {
                    // Now safe to get download URL
                    val downloadUrl = storageRef.downloadUrl.await().toString()

                    // Update Firestore user document
                    firestore.collection("users")
                        .document(userId)
                        .update("profileImageUrl", downloadUrl)
                        .addOnSuccessListener {
                            // Update local _userData so UI refreshes
                            _userData.value = _userData.value?.copy(profileImageUrl = downloadUrl)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update Firestore", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }



    // State updaters
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateUserClass(newUserClass: String) {
        _userClass.value = newUserClass
    }

    // Expose user data for convenience
    fun getLoggedInUser(): UserCredentials? = _userData.value
    val currentName: String get() = _userData.value?.name ?: ""
    val currentEmail: String get() = _userData.value?.email ?: ""
    val currentUserClass: String get() = _userData.value?.userClass ?: ""
}