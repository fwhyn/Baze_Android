package com.fwhyn.app.deandro.feature.func.auth.data.remote

import com.fwhyn.app.deandro.common.network.response.StatusInterface
import com.fwhyn.app.deandro.common.network.response.TimeInterface
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.lib.baze.common.data.model.Status
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("api/v1/auth/login/petugas")
    suspend fun login(
        @Body req: GetAuthTokenRepoParam.MyServer,
        @Query("force_login") forceLogin: Int = GetAuthTokenRepoParam.ForceLogin.NO.data,
    ): Response

    data class Response(
        override val status_code: Int,
        override var message: String,
        override var time: Int,
        @SerializedName("token") val dto: AuthTokenRaw.Dto,
    ) : StatusInterface, TimeInterface {

        val status: Status
            get() = Status.Instance(status_code, message)
    }
}