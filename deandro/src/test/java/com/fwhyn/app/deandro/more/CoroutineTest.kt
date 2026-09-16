package com.fwhyn.app.deandro.more

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class CoroutineTest {
    fun threadForKotlin() {
        val thread = Thread {
            Log.d("Test", "Running on thread: ${Thread.currentThread().name}")
        }
        thread.start()
        thread.join()
    }

    fun coroutineCheck() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            Log.d("Test", "Running in coroutine")
        }
    }

    suspend fun suspendCoroutine() {
        delay(1000.milliseconds)
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

    @Test
    fun testSuspend() = runTest {
        // TODO PR: apakah "after suspendCoroutine" akan muncul setelah "suspendCoroutine"?
        suspendCoroutine()
        Log.d("Test", "after suspendCoroutine")
    }
}