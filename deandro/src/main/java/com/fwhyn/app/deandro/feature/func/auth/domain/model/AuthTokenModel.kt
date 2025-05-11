package com.fwhyn.app.deandro.feature.func.auth.domain.model

sealed class AuthTokenModel {

    data class Data(
        val name: String,
        val code: String,
        val type: String,
        val userId: String,
    ) : AuthTokenModel()

    data object None : AuthTokenModel()
}