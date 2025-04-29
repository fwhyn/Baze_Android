package com.fwhyn.deandro.data.remote.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.deandro.data.local.auth.CredentialLocalDataSource
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInWaitingType @Inject constructor(
    private val credentialLocalDataSource: CredentialLocalDataSource,
) : GoogleSignIn(credentialLocalDataSource) {

    suspend fun signInAndGetResult(
        activity: Activity
    ): Rezult<GoogleIdTokenCredential, ErrorType> {

        return signInAndGetResult(activity, true)
    }

    private suspend fun signInAndGetResult(
        activity: Activity,
        loginUsingExistingUser: Boolean,
    ): Rezult<GoogleIdTokenCredential, ErrorType> {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleOption(loginUsingExistingUser))
            .build()

        val credentialResult = credentialLocalDataSource.getCredential(
            activityContext = activity,
            request = request
        )

        return when (credentialResult) {
            is Rezult.Failure -> {
                val result = credentialResult.err

                if (result is NoCredentialException) {
                    if (signInTry <= MAX_SIGN_IN_TRY) {
                        Log.d(debugTag, "Re-login")

                        signInTry++
                        signInAndGetResult(activity, false)
                    } else {
                        Log.e(debugTag, "No user granted")

                        signInTry = 0
                        getCredentialResult(null, ErrorType.NoUserGranted)
                    }
                } else {
                    Log.e(debugTag, "No user granted")

                    getCredentialResult(null, ErrorType.NoUserGranted)
                }
            }

            is Rezult.Success -> getCredentialResultSuccessCase(credentialResult.dat)
        }
    }

    private fun getCredentialResultSuccessCase(
        response: GetCredentialResponse
    ): Rezult<GoogleIdTokenCredential, ErrorType> {

        return if (isGoogleCredential(response)) {
            val credential = response.credential

            getCredentialResult(GoogleIdTokenCredential.createFrom(credential.data), ErrorType.None)
        } else {
            Log.e(debugTag, "Unexpected credential")

            getCredentialResult(null, ErrorType.UnexpectedCredential)
        }
    }

    private fun getCredentialResult(
        credential: GoogleIdTokenCredential?, errorType: ErrorType
    ): Rezult<GoogleIdTokenCredential, ErrorType> {
        googleIdTokenCredential = credential

        return if (credential != null) {
            Rezult.Success(credential)
        } else {
            Rezult.Failure(errorType)
        }
    }
}