package com.fwhyn.app.deandro.feature.func.auth.data.remote

import com.fwhyn.app.deandro.feature.func.auth.data.model.LoginParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("api/v1/auth/login/petugas")
    suspend fun login(
        @Body req: LoginParam,
        @Query("force_login") forceLogin: Int = LoginParam.ForceLogin.NO.data,
    ): LoginResponse
}