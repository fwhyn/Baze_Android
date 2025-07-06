package com.fwhyn.lib.baze.common.helper

import MainDispatcherRule
import android.util.Log
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.helper.extension.getDebugTag
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BaseRunnerSingleProcessTest {

    private val testTag = BaseRunnerSingleProcessTest::class.java.getDebugTag()

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
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        // Test with result enabled
        testInputEqualsOutput
            .setWithResult(true)
            .setResultNotifier { result ->
                when (result) {
                    is Rezult.Failure -> Util.throwMustNotFailed()
                    is Rezult.Success -> results.add(result)
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, (results[0] as Rezult.Success<String>).dat)

        // Test with result disabled
        results.clear()
        testInputEqualsOutput
            .setWithResult(false)
            .setResultNotifier { result ->
                Util.throwMustNotFailed() // Should not be called
            }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun setTimeOutMillisTest() = runTest {
        val testTimeOut = TestTimeOut()
        val scope = this

        testTimeOut
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Log.e(testTag, theError.toString())
                        Assert.assertEquals(true, theError is TimeoutCancellationException)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(Unit, scope)
    }

    @Test
    fun getIdTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this

        // Execute the use case
        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        val initialId = testInputEqualsOutput.getId()
        Assert.assertTrue(initialId.isNotEmpty())

        // Cancel the active job and verify the ID remains the same
        testInputEqualsOutput.cancelPreviousActiveJob()
        val sameId = testInputEqualsOutput.getId()
        Assert.assertEquals(initialId, sameId)

        // Execute a new job and verify the ID changes
        testInputEqualsOutput.execute(input, scope)
        val newId = testInputEqualsOutput.getId()
        Assert.assertNotEquals(initialId, newId)
    }

    @Test
    fun setUiContextTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        // Set a custom UI context
        val customUiContext = coroutineContext
        testInputEqualsOutput
            .setUiContext(customUiContext)
            .setResultNotifier { result ->
                when (result) {
                    is Rezult.Failure -> Util.throwMustNotFailed()
                    is Rezult.Success -> results.add(result)
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, (results[0] as Rezult.Success<String>).dat)
    }

    @Test
    fun setWorkerContextTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        // Set a custom worker context
        val customWorkerContext = coroutineContext
        testInputEqualsOutput
            .setWorkerContext(customWorkerContext)
            .setResultNotifier { result ->
                when (result) {
                    is Rezult.Failure -> Util.throwMustNotFailed()
                    is Rezult.Success -> results.add(result)
                }
            }
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, (results[0] as Rezult.Success<String>).dat)
    }

    @Test
    fun setResultNotifierTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        testInputEqualsOutput
            .setResultNotifier { result ->
                when (result) {
                    is Rezult.Failure -> Util.throwMustNotFailed()
                    is Rezult.Success -> results.add(result)
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(1, results.size)
        Assert.assertEquals(outputSuccess, (results[0] as Rezult.Success<String>).dat)
    }

    @Test
    fun setLifeCycleNotifierTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val lifeCycleEvents: ArrayList<BaseRunner.LifeCycle> = arrayListOf()

        testInputEqualsOutput
            .setLifeCycleNotifier { event -> lifeCycleEvents.add(event) }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(2, lifeCycleEvents.size)
        Assert.assertEquals(BaseRunner.LifeCycle.OnStart, lifeCycleEvents[0])
        Assert.assertEquals(BaseRunner.LifeCycle.OnFinish, lifeCycleEvents[1])
    }

    @Test
    fun outputShouldCorrespondTheInput() = runTest {
        val scope = this
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        val testInputEqualsOutput = TestInputEqualsOutput()
        val callback = { result: Rezult<String, Throwable> ->

            when (result) {
                is Rezult.Failure -> {
                    print(result.err.message)
                    Util.throwMustNotFailed()
                }

                is Rezult.Success -> {
                    results.add(result)
                    Assert.assertEquals(1, results.size)
                    Assert.assertEquals(outputSuccess, (results[0] as Rezult.Success<String>).dat)
                }
            }
        }
        testInputEqualsOutput
            .setResultNotifier(callback)
            .setWorkerContext(coroutineContext)
            .execute(input, scope)
    }

    @Test
    fun resultAndExecutionInvocation() = runTest {
        val testInputEqualsOutput = mock<TestInputEqualsOutput>()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testInputEqualsOutput.invoke(scope, { input }, callback)
        verify(testInputEqualsOutput, times(1)).invoke(eq(scope), eq({ input }), eq(callback))
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
                onGetParam = { input },
                result = callback
            )
    }

    @Test
    fun callbackOrderTest() = runTest {
        val scope = this
        val thisResults: ArrayList<String> = arrayListOf()
        val testInputEqualsOutput = TestInputEqualsOutput()
        val success = "success"

        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = this,
                onGetParam = { input },
                result = { result ->
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
            .invoke(scope, { input })

        val oldId = testInputEqualsOutput.getId()
        Assert.assertTrue(oldId.isNotEmpty())

        // Cancel the active job and verify the ID remains the same
        testInputEqualsOutput.cancelPreviousActiveJob()
        val sameId = testInputEqualsOutput.getId()
        Assert.assertEquals(oldId, sameId)

        // Start a new job and verify the ID changes
        testInputEqualsOutput(scope, { input })
        val newId = testInputEqualsOutput.getId()
        Assert.assertNotEquals(oldId, newId)
    }

    @Test
    fun joinTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        var isJobCompleted = false

        testInputEqualsOutput.invoke(
            scope = this,
            onGetParam = { input },
            result = { isJobCompleted = true }
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
                onGetParam = { },
                result = {
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
                onGetParam = { },
                result = {
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
        var resultSuccess = 0
        var resultFailed = 0

        override fun invoke(result: Result<String>) {
            result.onFailure {
                ++resultFailed
                Assert.assertEquals(1, resultFailed)
            }.onSuccess {
                ++resultSuccess
                Assert.assertEquals(1, resultSuccess)
            }
        }
    }
}