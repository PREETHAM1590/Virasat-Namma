package com.example.virasat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(AuthUiState())
    val loginState: StateFlow<AuthUiState> = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow(AuthUiState())
    val signUpState: StateFlow<AuthUiState> = _signUpState.asStateFlow()

    private val _resetState = MutableStateFlow(AuthUiState())
    val resetState: StateFlow<AuthUiState> = _resetState.asStateFlow()

    private val _googleSignInState = MutableStateFlow(AuthUiState())
    val googleSignInState: StateFlow<AuthUiState> = _googleSignInState.asStateFlow()

    val currentUser: FirebaseUser? get() = FirebaseAuthService.currentUser

    // ── Login ──────────────────────────────────────────────────────────────
    fun login(
        email: String,
        password: String,
        onSuccess: (String, String) -> Unit
    ) {
        val trimEmail = email.trim()
        if (trimEmail.isBlank() || password.isBlank()) {
            _loginState.value = AuthUiState(error = "Email and password are required.")
            return
        }
        _loginState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = FirebaseAuthService.signIn(trimEmail, password)
            result.fold(
                onSuccess = { user ->
                    _loginState.value = AuthUiState(success = true)
                    val name = user.displayName ?: trimEmail.substringBefore("@")
                    onSuccess(name, trimEmail)
                },
                onFailure = { e ->
                    _loginState.value = AuthUiState(error = FirebaseAuthService.friendlyError(e as Exception))
                }
            )
        }
    }

    // ── Sign Up ────────────────────────────────────────────────────────────
    fun signUp(
        name: String,
        email: String,
        password: String,
        onSuccess: (String, String) -> Unit
    ) {
        val trimName = name.trim()
        val trimEmail = email.trim()
        if (trimName.isBlank()) {
            _signUpState.value = AuthUiState(error = "Name is required.")
            return
        }
        if (trimEmail.isBlank()) {
            _signUpState.value = AuthUiState(error = "Email is required.")
            return
        }
        if (password.length < 6) {
            _signUpState.value = AuthUiState(error = "Password must be at least 6 characters.")
            return
        }
        _signUpState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = FirebaseAuthService.signUp(trimName, trimEmail, password)
            result.fold(
                onSuccess = {
                    _signUpState.value = AuthUiState(success = true)
                    onSuccess(trimName, trimEmail)
                },
                onFailure = { e ->
                    _signUpState.value = AuthUiState(error = FirebaseAuthService.friendlyError(e as Exception))
                }
            )
        }
    }

    // ── Password Reset ─────────────────────────────────────────────────────
    fun sendPasswordReset(email: String) {
        val trimEmail = email.trim()
        if (trimEmail.isBlank()) {
            _resetState.value = AuthUiState(error = "Enter your email address.")
            return
        }
        _resetState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = FirebaseAuthService.sendPasswordResetEmail(trimEmail)
            result.fold(
                onSuccess = { _resetState.value = AuthUiState(success = true) },
                onFailure = { e ->
                    _resetState.value = AuthUiState(error = FirebaseAuthService.friendlyError(e as Exception))
                }
            )
        }
    }

    // ── Google Sign-In ───────────────────────────────────────────────────
    fun signInWithGoogle(data: android.content.Intent?, onSuccess: (String, String) -> Unit) {
        _googleSignInState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = FirebaseAuthService.handleGoogleSignInResult(data)
            result.fold(
                onSuccess = { user ->
                    _googleSignInState.value = AuthUiState(success = true)
                    val name = user.displayName ?: user.email?.substringBefore("@") ?: "User"
                    val email = user.email ?: ""
                    onSuccess(name, email)
                },
                onFailure = { e ->
                    _googleSignInState.value = AuthUiState(
                        error = FirebaseAuthService.friendlyError(e as Exception)
                    )
                }
            )
        }
    }

    // ── Sign Out ───────────────────────────────────────────────────────────
    fun signOut() {
        FirebaseAuthService.signOut()
    }

    fun clearLoginError() { _loginState.value = AuthUiState() }
    fun clearGoogleSignInError() { _googleSignInState.value = AuthUiState() }
    fun clearSignUpError() { _signUpState.value = AuthUiState() }
    fun clearResetState() { _resetState.value = AuthUiState() }
}
