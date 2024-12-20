package com.fwhyn.deandro.data.remote.auth

import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.LoginResponse
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