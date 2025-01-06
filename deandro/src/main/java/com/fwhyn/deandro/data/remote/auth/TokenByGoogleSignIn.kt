package com.fwhyn.deandro.data.remote.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.data.model.Exzeption
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenByGoogleSignIn @Inject constructor() {

    companion object {
        private val TAG = TokenByGoogleSignIn::class.java.getTestTag()
        private const val WEB_CLIENT_ID = "269798095457-8gk7i3r85p4atv1tt4q0fgttpt3pgv3h.apps.googleusercontent.com"
    }

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .build()

    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    suspend fun getCredential(appContext: Context) {
        val credentialManager = CredentialManager.create(appContext)

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = appContext,
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            throw Exzeption(throwable = e.cause)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Do something
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }
}