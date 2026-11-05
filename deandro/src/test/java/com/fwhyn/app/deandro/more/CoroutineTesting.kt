package com.fwhyn.app.deandro.more

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dispatchers_shareOneScheduler() = runTest {
        val ioDispatcher = StandardTestDispatcher(testScheduler)
        val defaultDispatcher = StandardTestDispatcher(testScheduler)

        launch(ioDispatcher) {
            log("IO")
        }

        launch(defaultDispatcher) {
            log("Default")
        }

        advanceUntilIdle()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun standardTestDispatcher_logShowsQueuedExecution() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var state = "Initial"

        log("1. Before launch: state=$state")

        launch(dispatcher) {
            log("3. Coroutine body starts")
            state = "Updated"
            log("4. Coroutine changes state=$state")
        }

        log("2. After launch: state=$state")
        assertEquals("Initial", state)

        runCurrent()

        log("5. After runCurrent: state=$state")
        assertEquals("Updated", state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun unconfinedTestDispatcher_startImmediatelyButStillSuspends() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        var state = "Initial"

        launch(dispatcher) {
            state = "Started"
            log("Coroutine started: state=$state, time=${currentTime}ms")
            delay(1000.milliseconds)
            state = "Finished"
            log("Coroutine finished: state=$state, time=${currentTime}ms")
        }

        log("Immediately after launch: state=$state, time=${currentTime}ms")
        assertEquals("Started", state)

        advanceUntilIdle()

        log("After advanceUntilIdle: state=$state, time=${currentTime}ms")
        assertEquals("Finished", state)
    }
}