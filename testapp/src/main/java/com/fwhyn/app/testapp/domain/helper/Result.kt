package com.fwhyn.app.testapp.domain.helper

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
