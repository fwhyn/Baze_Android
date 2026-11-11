package com.fwhyn.app.deandro.feature.presentation.user

sealed interface UserUiState {

    data object Idle : UserUiState
    data object Loading : UserUiState

    data class Success(
        val name: String,
    ) : UserUiState

    data class Error(
        val message: String,
    ) : UserUiState
}