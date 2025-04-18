package com.fwhyn.deandro.data.remote.auth

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.client.http.HttpHeaders
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.drive.DriveScopes

object GoogleDriveAccess {

    const val REQUEST_AUTHORIZE: Int = 170121231

    private const val TAG = "GoogleDriveAccess"

    // ----------------------------------------------------------------
    private var onSuccess: OnSuccessCallback? = null
    private var onFailed: OnFailedCallback? = null

    @JvmStatic
    var authorizationResult: AuthorizationResult? = null
        private set

    @JvmStatic
    fun getGDriveAccess(
        activity: Activity,
        onSuccess: OnSuccessCallback,
        onFailed: OnFailedCallback,
    ) {
        this.onSuccess = onSuccess
        this.onFailed = onFailed

        val requestedScopes: List<Scope> = listOf(Scope(DriveScopes.DRIVE_FILE))
        val authorizationRequest = AuthorizationRequest
            .builder()
            .setRequestedScopes(requestedScopes)
            .build()

        Identity
            .getAuthorizationClient(activity)
            .authorize(authorizationRequest)
            .addOnSuccessListener { authorizationResult ->
                if (authorizationResult.hasResolution()) {
                    // Access needs to be granted by the user
                    val pendingIntent: PendingIntent? = authorizationResult.pendingIntent

                    try {
                        Log.d(TAG, "Get Google Drive access")

                        pendingIntent?.let {
                            startIntentSenderForResult(
                                activity,
                                pendingIntent.intentSender,
                                REQUEST_AUTHORIZE,
                                null,
                                0,
                                0,
                                0, null
                            )
                        }
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start Authorization UI: " + e.localizedMessage)

                        setAuthResult(null, ErrorType.UiPopUpFailed)
                    }
                } else {
                    setAuthResult(authorizationResult, ErrorType.None)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to authorize: " + e.localizedMessage)

                setAuthResult(null, ErrorType.AutorizationFailed)
            }
    }

    @JvmStatic
    fun extractResult(activity: Activity, data: Intent) {
        try {
            val authorizationResult = Identity.getAuthorizationClient(activity).getAuthorizationResultFromIntent(data)
            setAuthResult(authorizationResult, ErrorType.None)
        } catch (e: Exception) {
            setAuthResult(null, ErrorType.UnexpectedError)
        }

    }

    private fun setAuthResult(authorizationResult: AuthorizationResult?, errorType: ErrorType) {
        this.authorizationResult = authorizationResult

        if (authorizationResult != null) {
            Log.d(TAG, "Google Drive access has been granted")

            onSuccess?.onSuccess(authorizationResult)
            onSuccess = null
        } else {
            onFailed?.onFailed(errorType)
            onFailed = null
        }
    }

    @JvmStatic
    fun isAuthorized(): Boolean {
        return authorizationResult != null
    }

    @JvmStatic
    fun getHttpRequestInitializer(timeoutMillis: Int): HttpRequestInitializer {
        return HttpRequestInitializer { request: HttpRequest ->
            request.headers = HttpHeaders().apply {
                authorization = "Bearer ${authorizationResult?.accessToken}"
            }

            // Set connection timeout and read timeout
            request.setConnectTimeout(timeoutMillis)
            request.setReadTimeout(timeoutMillis)
        }
    }

    // ----------------------------------------------------------------
    interface OnSuccessCallback {
        fun onSuccess(result: AuthorizationResult)
    }

    interface OnFailedCallback {
        fun onFailed(errorType: ErrorType)
    }

    enum class ErrorType {
        None,
        UiPopUpFailed,
        AutorizationFailed,
        UnexpectedError,
    }
}