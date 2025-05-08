package com.fwhyn.deandro.data.remote.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.fwhyn.deandro.BuildConfig
import com.fwhyn.deandro.data.local.auth.CredentialLocalDataSource
import com.fwhyn.lib.baze.data.helper.extension.continueIfActive
import com.fwhyn.lib.baze.data.helper.extension.getDebugTag
import com.fwhyn.lib.baze.domain.helper.Rezult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
open class GoogleSignIn @Inject constructor(
    private val credentialLocalDataSource: CredentialLocalDataSource,
) {

    companion object {
        const val MAX_SIGN_IN_TRY = 3
    }

    val debugTag = GoogleSignIn::class.java.getDebugTag()

    // ----------------------------------------------------------------
    private var googleIdTokenCredential: GoogleIdTokenCredential? = null
    private var onFinishedCallback: ((Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit)? = null

    private var signInTry = 0

    fun getGoogleIdTokenCredential(): GoogleIdTokenCredential? = googleIdTokenCredential

    suspend fun signInAndGetResult(
        activity: Activity
    ): Rezult<GoogleIdTokenCredential, ErrorType> {
        val context = coroutineContext

        return suspendCancellableCoroutine { continuation ->

            signIn(activity, CoroutineScope(context)) {
                continuation.continueIfActive(it)
            }
        }

    }

    fun signIn(
        activity: Activity,
        coroutineScope: CoroutineScope,
        onFinished: (Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit,
    ) {
        signIn(activity, coroutineScope, true, onFinished)
    }

    private fun signIn(
        activity: Activity,
        coroutineScope: CoroutineScope,
        loginUsingExistingUser: Boolean,
        onFinished: (Rezult<GoogleIdTokenCredential, ErrorType>) -> Unit,
    ) {
        onFinishedCallback = onFinished

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleOption(loginUsingExistingUser))
            .build()

        coroutineScope.launch {
            val credentialResult = credentialLocalDataSource.getCredential(
                activityContext = activity,
                request = request
            )

            when (credentialResult) {
                is Rezult.Failure -> {
                    val result = credentialResult.err

                    if (result is NoCredentialException) {
                        if (signInTry <= MAX_SIGN_IN_TRY) {
                            Log.d(debugTag, "Re-login")

                            signIn(activity, coroutineScope, false, onFinished)
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
                    val result = credentialResult.dat
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