package com.fwhyn.appsample.data.model.auth

import com.fwhyn.appsample.data.model.response.StatusInterface
import com.fwhyn.appsample.data.model.response.TimeInterface
import com.fwhyn.data.model.Status
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    override val status_code: Int,
    override var message: String,
    override var time: Int,
    @SerializedName("token") val userToken: UserToken,
) : StatusInterface, TimeInterface {
    val status: Status
        get() = Status.Instance(status_code, message)
}