package com.fwhyn.app.deandro.feature.presentation.user

interface UserRepo {
    suspend fun getUserName(): String
}