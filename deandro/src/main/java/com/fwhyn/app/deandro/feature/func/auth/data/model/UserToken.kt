package com.fwhyn.app.deandro.feature.func.auth.data.model

import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("name") var name: String = "",
    @SerializedName("code") var code: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("user_id") var userId: String = "",
)