package com.fwhyn.app.deandro.more

import MainDispatcherRule
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        // TODO PR supaya log nya muncul di test
        coroutineCheck()
    }

//    @Test
//    fun testSuspend() = runTest {
//        // TODO PR: apakah "after suspendCoroutine" akan muncul setelah "suspendCoroutine"?
//        suspendCoroutine()
//        Log.d("Test", "after suspendCoroutine")
//    }

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
}