package com.fwhyn.app.deandro.more

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class CoroutineTesting {
    private fun log(message: String) {
        println("[DEMO] $message")
    }

    private fun realElapsedMillis(startNanos: Long): Long {
        return (System.nanoTime() - startNanos) / 1_000_000
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exampleWithRunTest() {
        val startNanos = System.nanoTime()
        runTest {
            log("Before delay: virtualTime=${currentTime}ms")
            delay(1000.milliseconds)
            log("After delay: virtualTime=${currentTime}ms")
            assertEquals(1000.milliseconds, currentTime.milliseconds)
        }

        log("Real elapsed time = ${realElapsedMillis(startNanos)}ms")
    }

    @Test
    fun exampleWithRunBlocking() {
        val startNanos = System.nanoTime()
        runBlocking {
            log("Before delay")
            delay(1000.milliseconds)
            log("After delay")
        }

        log("Real elapsed time = ${realElapsedMillis(startNanos)}ms")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun launchedCoroutine_requiresSchedulerProgress() = runTest {
        var result = "Initial"
        launch {
            result = "Updated"
        }

        assertEquals("Initial", result)
        runCurrent()
        assertEquals("Updated", result)
    }

    @Test
    fun launchedCoroutine_canBeJoined() = runTest {
        var result = "Initial"
        val job = launch {
            delay(1000.milliseconds)
            result = "Updated"
        }

        val job2 = launch {
            delay(1500.milliseconds)
            result = "Updated Job2"
        }

        assertEquals("Initial", result)
        job.join()
//        job2.join()
        log("Current virtualTime=${currentTime}ms")
        assertEquals("Updated", result)
    }
}