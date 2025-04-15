package com.fwhyn.deandro.data.remote.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.deandro.BuildConfig
import com.fwhyn.deandro.data.local.auth.CredentialLocalDataSource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignIn @Inject constructor(
    private val credentialLocalDataSource: CredentialLocalDataSource,
) {

    companion object {
        private const val MAX_SIGN_IN_TRY = 3
    }

    val debugTag = GoogleSignIn::class.java.getTestTag()

    // ----------------------------------------------------------------
    var googleIdTokenCredential: GoogleIdTokenCredential? = null
        private set
    private var onFinishedCallback: ((Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit)? = null

    private var signInTry = 0

    suspend fun signIn(
        activity: Activity,
        onFinished: (Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit,
    ) {
        signIn(activity, true, onFinished)
    }

    private suspend fun signIn(
        activity: Activity,
        loginUsingExistingUser: Boolean,
        onFinished: (Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit,
    ) {
        onFinishedCallback = onFinished

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleOption(loginUsingExistingUser))
            .build()

        credentialLocalDataSource.getCredential(
            activityContext = activity,
            request = request
        ) {
            when (it) {
                is Rezult.Failure -> {
                    val result = it.err

                    if (result is NoCredentialException) {
                        Log.d(debugTag, "Re-login")

                        if (signInTry <= MAX_SIGN_IN_TRY) {
                            signIn(activity, false, onFinished)
                            signInTry++
                        } else {
                            Log.e(debugTag, "No user granted")

                            setCredentialResult(null, ErrorType.NoUserGranted)
                            signInTry = 0
                        }
                    } else {
                        Log.e(debugTag, "No user granted")

                        setCredentialResult(null, ErrorType.NoUserGranted)
                    }
                }

                is Rezult.Success -> {
                    val result = it.dat
                    if (isGoogleCredential(result)) {
                        val credential = result.credential

                        setCredentialResult(GoogleIdTokenCredential.createFrom(credential.data), ErrorType.None)
                    } else {
                        Log.e(debugTag, "Unexpected credential")

                        setCredentialResult(null, ErrorType.UnexpectedCredential)
                    }
                }
            }
        }
    }

    private fun getGoogleOption(loginUsingExistingUser: Boolean): GetGoogleIdOption {
        val builder = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(loginUsingExistingUser)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)

        if (loginUsingExistingUser) {
            builder.setAutoSelectEnabled(true)
        }

        return builder.build()
    }

    private fun isGoogleCredential(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        return credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    }

    private suspend fun setCredentialResult(credential: GoogleIdTokenCredential?, errorType: ErrorType) {
        googleIdTokenCredential = credential

        withContext(Dispatchers.Main) {
            if (credential != null) {
                onFinishedCallback?.invoke(Rezult.Success(credential))
            } else {
                onFinishedCallback?.invoke(Rezult.Failure(errorType))
            }
        }

        onFinishedCallback = null
    }

    // ----------------------------------------------------------------

    enum class ErrorType {
        None,
        UnexpectedCredential,
        NoUserGranted
    }
}