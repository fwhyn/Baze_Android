package com.fwhyn.app.deandro.more

import MainDispatcherRule
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class CoroutineTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    fun threadForKotlin() {
        val thread = Thread {
            Log.d("Test", "Running on thread: ${Thread.currentThread().name}")
        }
        thread.start()
        thread.join()
    }

    fun coroutineCheck() = runTest {
        val scope = this
        scope.launch {
            Log.d("Test", "Running in coroutine")
        }
    }

    suspend fun suspendCoroutine() {
        delay(3000.milliseconds)
        Log.d("Test", "suspendCoroutine")
    }


    @Test
    fun testThread() {
        threadForKotlin()
    }

    @Test
    fun testCoroutine() {
        coroutineCheck()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSuspend() = runTest {
        var resumed = false

        val job = launch {
            suspendCoroutine()
            resumed = true
        }

        runCurrent()
        assertFalse(resumed)

        advanceTimeBy(2999.milliseconds)
        runCurrent()
        assertFalse(resumed)

        advanceTimeBy(1.milliseconds)
        runCurrent()
        assertTrue(resumed)

        job.join()
    }

    @Test
    fun testAsync() = runTest {
        val result = async {
            delay(1000.milliseconds)
            10 + 20
        }

        println("Doing other work...")

        println("Result = ${result.await()}")
    }

    suspend fun getUser(): String {
        delay(1000.milliseconds)
        return "User"
    }

    suspend fun getPosts(): List<String> {
        delay(1000.milliseconds)
        return listOf("Post 1", "Post 2")
    }

    @Test
    fun testParalelAsync() = runTest {
        val user = async {
            getUser()
        }

        val posts = async {
            getPosts()
        }

        println(user.await())
        println(posts.await())
    }

    @Test
    fun testParalelLaunch() = runTest {
        val user = launch {
            getUser()
        }

        val posts = launch {
            getPosts()
        }

        println(user)
        println(posts)
    }
}