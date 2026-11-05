package com.fwhyn.app.deandro.feature.presentation.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class UserRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userApi: UserApi = UserApi()
) {

    suspend fun getUser(): User {
        return userApi.getUser()
    }

    suspend fun loadUser(): String = withContext(dispatcher) {
        delay(1000.milliseconds)
        "Yana"
    }

    suspend fun fetchUser(): String {
        delay(1000.milliseconds)
        return "Yana"
    }

    suspend fun getPosts(): List<Post> {
        return withContext(Dispatchers.IO) {
            userApi.getPosts()
        }
    }
}
