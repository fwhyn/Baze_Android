package com.fwhyn.lib.baze.common.helper

import MainDispatcherRule
import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseRunnerSingleProcessTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val input = "Yana"
    private val outputSuccess = "Output: Yana"

    // ----------------------------------------------------------------
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // ----------------------------------------------------------------
    @Test
    fun dataShouldBeHelloWorld() = runTest {
        val data = fetchData()
        Assert.assertEquals("Hello world", data)
    }

    private suspend fun fetchData(): String {
        delay(1000)
        return "Hello world"
    }

    @Test
    fun setWithResultTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<String> = arrayListOf()

        // Test with result enabled
        testInputEqualsOutput.setWorkerContext(coroutineContext)


        testInputEqualsOutput.invoke(
            scope = scope,
            onFetchParam = { input },
            onOmitResult = {
                it.onSuccess { output ->
                    results.add(output)
                }.onFailure {
                    Util.throwMustNotFailed()
                }
            }
        )

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, results[0])
    }

    @Test
    fun setTimeOutMillisTest() = runTest {
        val testTimeOut = TestTimeOut()
        val scope = this

        testTimeOut
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = scope,
                onFetchParam = { },
                onOmitResult = {
                    it.onSuccess {
                        Util.throwMustNotSuccess()
                    }.onFailure { output ->
                        Assert.assertEquals(true, output is TimeoutCancellationException)
                    }

                }
            )
    }

    @Test
    fun getIdTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this

        // Execute the use case
        testInputEqualsOutput.setWorkerContext(coroutineContext)
        testInputEqualsOutput.invoke(
            scope = scope,
            onFetchParam = { input }
        )

        val initialId = testInputEqualsOutput.getId()
        Assert.assertTrue(initialId.isNotEmpty())

        // Cancel the active job and verify the ID remains the same
        testInputEqualsOutput.cancelPreviousActiveJob()
        val sameId = testInputEqualsOutput.getId()
        Assert.assertEquals(initialId, sameId)

        // Execute a new job and verify the ID changes
        testInputEqualsOutput.invoke(
            scope = scope,
            onFetchParam = { input }
        )
        val newId = testInputEqualsOutput.getId()
        Assert.assertNotEquals(initialId, newId)
    }

    @Test
    fun setWorkerContextTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<String> = arrayListOf()

        // Set a custom worker context
        val customWorkerContext = coroutineContext
        testInputEqualsOutput
            .setWorkerContext(customWorkerContext)
            .invoke(
                scope = scope,
                onFetchParam = { input },
                onOmitResult = { result ->
                    result.onSuccess {
                        results.add(it)
                    }.onFailure {
                        Util.throwMustNotFailed()
                    }
                }
            )

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, results[0])
    }

    @Test
    fun setUiContextTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val lifeCycle: ArrayList<String> = arrayListOf()
        val start = "start"
        val finish = "finish"

        // Set a custom UI context
        val customUiContext = coroutineContext
        testInputEqualsOutput
            .setUiContext(customUiContext)
            .invoke(
                scope = scope,
                onStart = { lifeCycle.add(start) },
                onFetchParam = { input },
                onFinish = { lifeCycle.add(finish) },
            )

        testInputEqualsOutput.join()
        Assert.assertEquals(2, lifeCycle.size)
        Assert.assertEquals(start, lifeCycle[0])
        Assert.assertEquals(finish, lifeCycle[1])
    }

    @Test
    fun outputShouldCorrespondTheInput() = runTest {
        val scope = this
        val results: ArrayList<String> = arrayListOf()

        val testInputEqualsOutput = TestInputEqualsOutput()

        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = scope,
                onFetchParam = { input },
                onOmitResult = {
                    it.onSuccess { output ->
                        results.add(output)
                        Assert.assertEquals(1, results.size)
                        Assert.assertEquals(outputSuccess, results[0])
                    }.onFailure { error ->
                        Log.d("Error message", error.message ?: "No message")
                        Util.throwMustNotFailed()
                    }
                }
            )
    }

    @Test
    fun callbackInvocationTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = scope,
                onFetchParam = { input },
                onOmitResult = callback
            )
    }

    @Test
    fun callbackOrderTest() = runTest {
        val thisResults: ArrayList<String> = arrayListOf()
        val testInputEqualsOutput = TestInputEqualsOutput()
        val success = "success"

        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = this,
                onFetchParam = { input },
                onOmitResult = { result ->
                    result.onSuccess {
                        thisResults.add(success)
                    }.onFailure {
                        Util.throwMustNotFailed()
                    }
                }
            )

        testInputEqualsOutput.join()

        Assert.assertEquals(1, thisResults.size)
        Assert.assertEquals(success, thisResults[0])
    }

    @Test
    fun cancelJobIfActiveTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this

        // Start the first job
        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .setTimeOutMillis(10000)
            .invoke(scope = scope, onFetchParam = { input })

        val oldId = testInputEqualsOutput.getId()
        Assert.assertTrue(oldId.isNotEmpty())

        // Cancel the active job and verify the ID remains the same
        testInputEqualsOutput.cancelPreviousActiveJob()
        val sameId = testInputEqualsOutput.getId()
        Assert.assertEquals(oldId, sameId)

        // Start a new job and verify the ID changes
        testInputEqualsOutput(scope = scope, onFetchParam = { input })
        val newId = testInputEqualsOutput.getId()
        Assert.assertNotEquals(oldId, newId)
    }

    @Test
    fun joinTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        var isJobCompleted = false

        testInputEqualsOutput.invoke(
            scope = this,
            onFetchParam = { input },
            onOmitResult = { isJobCompleted = true }
        )

        testInputEqualsOutput.join()
        Assert.assertTrue(isJobCompleted)
    }

    @Test
    fun exceptionErrorTest() = runTest {
        val exceptionError = ExceptionError()

        repeat(2) {
            exceptionError.invoke(
                scope = this,
                onFetchParam = { },
                onOmitResult = {
                    it.onSuccess {
                        Util.throwMustNotSuccess()
                    }.onFailure { error ->
                        Assert.assertTrue(error is Exception)
                    }
                }
            )
            exceptionError.join()
        }
    }

    @Test
    fun securityExceptionErrorTest() = runTest {
        val securityExceptionError = SecurityExceptionError()

        repeat(2) {
            securityExceptionError.invoke(
                scope = this,
                onFetchParam = { },
                onOmitResult = {
                    it.onSuccess {
                        Util.throwMustNotSuccess()
                    }.onFailure { error ->
                        Assert.assertTrue(error is SecurityException)
                    }
                }
            )
            securityExceptionError.join()
        }
    }

    // ----------------------------------------------------------------
    class ExceptionError : BaseRunner<Unit, Unit>() {

        override suspend fun onRunning(param: Unit, result: suspend (Unit) -> Unit) {
            throw Exception()
        }
    }

    class SecurityExceptionError : BaseRunner<Unit, Unit>() {

        override suspend fun onRunning(param: Unit, result: suspend (Unit) -> Unit) {
            throw SecurityException()
        }
    }

    class TestInputEqualsOutput : BaseRunner<String, String>() {

        override suspend fun onRunning(param: String, result: suspend (String) -> Unit) {
            delay(1000)
            result("Output: $param")
        }
    }

    class TestTimeOut : BaseRunner<Unit, Unit>() {

        init {
            setTimeOutMillis(1000)
        }

        override suspend fun onRunning(param: Unit, result: suspend (Unit) -> Unit) {
            loading()
        }

        private suspend fun loading() {
            while (true) {
                delay(1)
            }
        }
    }

    class CallbackInvocation : (Result<String>) -> Unit {
        private var resultSuccess = 0
        private var resultFailed = 0

        override fun invoke(result: Result<String>) {
            result.onFailure { error ->
                Log.d("Error message", error.message ?: "No message")
                ++resultFailed
                Assert.assertEquals(1, resultFailed)
            }.onSuccess {
                ++resultSuccess
                Assert.assertEquals(1, resultSuccess)
            }
        }
    }
}