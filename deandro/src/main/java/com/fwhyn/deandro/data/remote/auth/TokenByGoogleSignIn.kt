package com.fwhyn.deandro.data.remote.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenByGoogleSignIn @Inject constructor() {

    companion object {
        const val WEB_CLIENT_ID = "269798095457-8gk7i3r85p4atv1tt4q0fgttpt3pgv3h.apps.googleusercontent.com"
    }

//    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(true)
//        .setServerClientId(WEB_CLIENT_ID)
//        .setAutoSelectEnabled(true)
//        .setNonce(<nonce string to use when generating a Google ID token>)
//    .build()
//
//    private fun generateNonce(): String {
//        val nonceBytes = ByteArray(16)
//        SecureRandom().nextBytes(nonceBytes)
//        return Base64.getUrlEncoder().encodeToString(nonceBytes)
//    }
}