package com.fwhyn.app.deandro.feature.presentation.user

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRepositoryTest {
    val tag = "UserRepositoryTest"

    @Test
    fun testLoadUserRepository() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = UserRepository(dispatcher = dispatcher)

        val result = repository.loadUser()
        assertEquals("Yana", result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFetchUserRepository() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = UserRepository(dispatcher = dispatcher)

        var result = "Initial"
        Log.d(tag, "Before fetchUser: ${currentTime}ms")

        val job = launch {
            result = repository.fetchUser()
            Log.d(tag, "After fetchUser: ${currentTime}ms")
        }

//        advanceTimeBy(1100.milliseconds)
//        Log.d(tag, "After advanceTimeBy 1100ms: ${currentTime}ms")

//        runCurrent()
//        Log.d(tag, "After runCurrent: ${currentTime}ms")

        Log.d(tag, "Before job.join(): ${currentTime}ms")
        job.join()
        Log.d(tag, "After job.join(): ${currentTime}ms")

        assertEquals("Yana", result)
    }
}