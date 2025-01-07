package com.fwhyn.deandro.data.local.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import com.fwhyn.baze.data.helper.extension.get
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.data.helper.extension.put
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.di.PreferenceModule
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenLocalDataSource @Inject constructor(
    @PreferenceModule.EncryptedPrefs private val encryptedPreferences: SharedPreferences,
    @ApplicationContext context: Context,
) {
    companion object {
        val TAG = TokenLocalDataSource::class.java.getTestTag()
        const val TOKEN_KEY = "TOKEN_KEY"
    }

    private val credentialManager = CredentialManager.create(context)

    var token: UserToken? = null
        get() {
            return if (field == null) {
                field = encryptedPreferences.get<UserToken>(TOKEN_KEY)
                field
            } else {
                field
            }
        }
        set(value) {
            field = value
            encryptedPreferences.put(TOKEN_KEY, field)
        }

    suspend fun getCredential(activityContext: Context) {
        val getPasswordOption = GetPasswordOption()
        val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(
            requestJson = requestJson
        )
        val getCredRequest = GetCredentialRequest(
            listOf(getPublicKeyCredentialOption, getPasswordOption)
        )

        try {
            val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                credentialManager.prepareGetCredential(getCredRequest)
            } else {
                TODO("VERSION.SDK_INT < UPSIDE_DOWN_CAKE")
            }

            val result = credentialManager.getCredential(
                context = activityContext,
                request = getCredRequest
            )

            handleSignIn(result)
        } catch (e: NoCredentialException) {
            Log.e(TAG, "No credential available", e)
        } catch (e: GetCredentialException) {
            onGetCredentialFailed(e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is PublicKeyCredential -> {
                val responseJson = credential.authenticationResponseJson
                // Share responseJson i.e. a GetCredentialResponse on your server to
                // validate and  authenticate
            }

            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                // Use id and password to send to your server to validate
                // and authenticate
            }

            is CustomCredential -> {
                // If you are also using any external sign-in libraries, parse them
                // here with the utility functions provided.
                if (credential.type == ExampleCustomCredential.TYPE) {
                    try {
                        val ExampleCustomCredential = ExampleCustomCredential.createFrom(credential.data)
                        // Extract the required credentials and complete the authentication as per
                        // the federated sign in or any external sign in library flow
                    } catch (e: ExampleCustomCredential.ExampleCustomCredentialParsingException) {
                        // Unlikely to happen. If it does, you likely need to update the dependency
                        // version of your external sign-in library.
                        Log.e(TAG, "Failed to parse an ExampleCustomCredential", e)
                    }
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


    @SuppressLint("PublicKeyCredential")
    @RequiresApi(28)
    suspend fun createPasskey(
        activityContext: Context,
        requestJson: String,
        preferImmediatelyAvailableCredentials: Boolean,
    ) {
        val createPublicKeyCredentialRequest = CreatePublicKeyCredentialRequest(
            // Contains the request in JSON format. Uses the standard WebAuthn
            // web JSON spec.
            requestJson = requestJson,
            // Defines whether you prefer to use only immediately available
            // credentials, not hybrid credentials, to fulfill this request.
            // This value is false by default.
            preferImmediatelyAvailableCredentials = preferImmediatelyAvailableCredentials,
        )

        // Execute CreateCredentialRequest asynchronously to register credentials
        // for a user account. Handle success and failure cases with the result and
        // exceptions, respectively.
        try {
            val result = credentialManager.createCredential(
                // Use an activity-based context to avoid undefined system
                // UI launching behavior
                context = activityContext,
                request = createPublicKeyCredentialRequest,
            )
            handlePasskeyRegistrationResult(result)
        } catch (e: CreateCredentialException) {
            onCreatePasskeyFailed(e)
        }
    }

    private fun onCreatePasskeyFailed(e: CreateCredentialException) {
        when (e) {
            is CreatePublicKeyCredentialDomException -> {
                // Handle the passkey DOM errors thrown according to the
                // WebAuthn spec.
                handlePasskeyError(e.domError)
            }

            is CreateCredentialCancellationException -> {
                // The user intentionally canceled the operation and chose not
                // to register the credential.
            }

            is CreateCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
            }

            is CreateCredentialProviderConfigurationException -> {
                // Your app is missing the provider configuration dependency.
                // Most likely, you're missing the
                // "credentials-play-services-auth" module.
            }

            is CreateCredentialUnknownException -> {
                TODO("Need Implementation")
            }

            is CreateCredentialCustomException -> {
                // You have encountered an error from a 3rd-party SDK. If you
                // make the API call with a request object that's a subclass of
                // CreateCustomCredentialRequest using a 3rd-party SDK, then you
                // should check for any custom exception type constants within
                // that SDK to match with e.type. Otherwise, drop or log the
                // exception.
            }

            else -> Log.w(TAG, "Unexpected exception type ${e::class.java.name}")
        }
    }

    suspend fun registerPassword(
        activityContext: Context,
        username: String,
        password: String,
    ) {
        // Initialize a CreatePasswordRequest object.
        val createPasswordRequest =
            CreatePasswordRequest(id = username, password = password)

        // Create credential and handle result.
        try {
            val result =
                credentialManager.createCredential(
                    // Use an activity based context to avoid undefined
                    // system UI launching behavior.
                    activityContext,
                    createPasswordRequest
                )

            handleRegisterPasswordResult(result)
        } catch (e: CreateCredentialException) {
            onRegisterPasswordFailed(e)
        }
    }
}