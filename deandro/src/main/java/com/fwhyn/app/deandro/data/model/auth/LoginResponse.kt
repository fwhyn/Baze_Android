package com.fwhyn.app.deandro.data.model.auth

import com.fwhyn.app.deandro.data.model.response.StatusInterface
import com.fwhyn.app.deandro.data.model.response.TimeInterface
import com.fwhyn.lib.baze.data.model.Status
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