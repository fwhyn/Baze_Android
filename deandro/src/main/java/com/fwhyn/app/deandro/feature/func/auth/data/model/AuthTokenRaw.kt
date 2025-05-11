package com.fwhyn.app.deandro.feature.func.auth.data.model

import com.google.gson.annotations.SerializedName


sealed class AuthTokenRaw {

    data class Dto(
        @SerializedName("name") val name: String = "",
        @SerializedName("code") val code: String = "",
        @SerializedName("type") val type: String = "",
        @SerializedName("user_id") val userId: String = "",
    ) : AuthTokenRaw()

    data object None : AuthTokenRaw()
}