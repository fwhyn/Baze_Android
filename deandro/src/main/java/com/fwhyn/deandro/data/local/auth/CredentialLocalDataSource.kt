package com.fwhyn.deandro.data.local.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.fwhyn.baze.domain.helper.Rezult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val credentialManager: CredentialManager by lazy { CredentialManager.create(context) }

    suspend fun getCredential(
        activityContext: Context,
        request: GetCredentialRequest,
        onFinished: suspend (result: Rezult<GetCredentialResponse, GetCredentialException>) -> Unit,
    ) {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext,
            )
            onFinished(Rezult.Success(result))
        } catch (e: GetCredentialException) {
            onFinished(Rezult.Failure(e))
        }
    }
}