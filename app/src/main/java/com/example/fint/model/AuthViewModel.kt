package com.example.fint.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase
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

    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

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

    fun register(name: String, email: String, password: String, grade: String, context: Context) {
        val cleanEmail = email.trim()
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(cleanEmail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val newUser = UserCredentials(
                            userId = userId,
                            name = name,
                            email = cleanEmail,
                            userClass = grade,
                            profileImageUrl = "" // default empty or placeholder
                        )

                        // Save to Realtime Database
                        FirebaseDatabase.getInstance().reference
                            .child("users")
                            .child(userId)
                            .setValue(newUser)
                            .addOnSuccessListener {
                                // Also save to Firestore
                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(userId)
                                    .set(newUser)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                                        _userData.value = newUser
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Failed to save to Firestore", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save to Realtime DB", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
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
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("users").child(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value as? String ?: ""
                val email = snapshot.child("email").value as? String ?: ""
                val profileImageUrl = snapshot.child("profileImageUrl").value as? String ?: ""

                val user = UserCredentials(
                    userId = userId,
                    name = name,
                    email = email,
                    profileImageUrl = profileImageUrl
                )

                _userData.value = user
            }
            .addOnFailureListener {
                Log.e("AuthViewModel", "Failed to load user data: ${it.message}")
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

        // Validate URI
        if (uri.scheme != "content" && uri.scheme != "file") {
            Toast.makeText(context, "Invalid image URI", Toast.LENGTH_SHORT).show()
            return
        }

        val imageRef = storage.reference.child("profileImages/$userId.jpg")

        viewModelScope.launch {
            try {
                Log.d("UploadImage", "Uploading URI: $uri")

                // Upload the file to Firebase Storage
                imageRef.putFile(uri).await()

                // Get download URL
                val downloadUrl = imageRef.downloadUrl.await().toString()

                // Save URL to Realtime Database
                val userRef = database.reference.child("users").child(userId)
                userRef.child("profileImageUrl").setValue(downloadUrl).await()

                // Optional: Refresh user data or update state
                fetchUserData() // Ensures UI reflects the updated profile image

                Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("UploadImage", "Upload failed", e)
                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    // Expose user data for convenience
    fun getLoggedInUser(): UserCredentials? = _userData.value
    val currentName: String get() = _userData.value?.name ?: ""
    val currentEmail: String get() = _userData.value?.email ?: ""
    val currentUserClass: String get() = _userData.value?.userClass ?: ""
}


