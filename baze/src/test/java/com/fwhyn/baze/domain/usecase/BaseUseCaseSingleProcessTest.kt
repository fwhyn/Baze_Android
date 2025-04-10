package com.fwhyn.baze.domain.usecase

import MainDispatcherRule
import android.util.Log
import com.fwhyn.baze.data.helper.Util
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.data.model.Exzeption
import com.fwhyn.baze.domain.helper.Rezult
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BaseUseCaseSingleProcessTest {

    val testTag = BaseUseCaseSingleProcessTest::class.java.getTestTag()

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

    suspend fun fetchData(): String {
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
        val lifeCycleEvents: ArrayList<BaseUseCase.LifeCycle> = arrayListOf()

        testInputEqualsOutput
            .setLifeCycleNotifier { event -> lifeCycleEvents.add(event) }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertEquals(2, lifeCycleEvents.size)
        Assert.assertEquals(BaseUseCase.LifeCycle.OnStart, lifeCycleEvents[0])
        Assert.assertEquals(BaseUseCase.LifeCycle.OnFinish, lifeCycleEvents[1])
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

        testInputEqualsOutput.setResultNotifier(callback)
        verify(testInputEqualsOutput, times(1)).setResultNotifier(eq(callback))

        testInputEqualsOutput.execute(input, scope)
        verify(testInputEqualsOutput, times(1)).execute(eq(input), eq(scope))
    }

    @Test
    fun callbackInvocationTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testInputEqualsOutput
            .setResultNotifier(callback)
            .setWorkerContext(coroutineContext)
            .execute(input, scope)
    }

    @Test
    fun callbackOrderTest() = runTest {
        val scope = this
        val thisResults: ArrayList<String> = arrayListOf()
        val testInputEqualsOutput = TestInputEqualsOutput()
        val start = "start"
        val finish = "finish"
        val success = "success"

        testInputEqualsOutput
            .setLifeCycleNotifier {
                when (it) {
                    BaseUseCase.LifeCycle.OnStart -> thisResults.add(start)
                    BaseUseCase.LifeCycle.OnFinish -> thisResults.add(finish)
                }
            }
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        print(it.err.message)
                        Util.throwMustNotFailed()
                    }

                    is Rezult.Success -> thisResults.add(success)
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(input, scope)

        testInputEqualsOutput.join()

        Assert.assertEquals(start, thisResults[0])
        Assert.assertEquals(success, thisResults[1])
        Assert.assertEquals(finish, thisResults[2])
    }

    @Test
    fun cancelJobIfActiveTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this

        // Start the first job
        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .setTimeOutMillis(10000)
            .execute(input, scope)

        val oldId = testInputEqualsOutput.getId()
        Assert.assertTrue(oldId.isNotEmpty())

        // Cancel the active job and verify the ID remains the same
        testInputEqualsOutput.cancelPreviousActiveJob()
        val sameId = testInputEqualsOutput.getId()
        Assert.assertEquals(oldId, sameId)

        // Start a new job and verify the ID changes
        testInputEqualsOutput.execute(input, scope)
        val newId = testInputEqualsOutput.getId()
        Assert.assertNotEquals(oldId, newId)
    }

    @Test
    fun joinTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        var isJobCompleted = false

        testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .setResultNotifier { isJobCompleted = true }
            .execute(input, scope)

        testInputEqualsOutput.join()
        Assert.assertTrue(isJobCompleted)
    }

    @Test
    fun getResultInBackgroundTest() = runTest {
        val scope = this
        val testInputEqualsOutput = TestInputEqualsOutput()

        val output = testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .getResult(input, scope)

        Assert.assertEquals(outputSuccess, output)
    }

    @Test
    fun exzeptionErrorTest() = runTest {
        val exzeptionError = ExzeptionError()
        val scope = this

        exzeptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Exzeption)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(Unit, scope)

        exzeptionError.join()

        exzeptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Exzeption)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .execute(Unit)
    }

    @Test
    fun exceptionErrorTest() = runTest {
        val exceptionError = ExceptionError()
        val scope = this

        exceptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Exception)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(Unit, scope)

        exceptionError.join()

        exceptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Exception)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .execute(Unit)
    }

    @Test
    fun securityExceptionErrorTest() = runTest {
        val securityExceptionError = SecurityExceptionError()
        val scope = this

        securityExceptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is SecurityException)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(Unit, scope)

        securityExceptionError.join()

        securityExceptionError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is SecurityException)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .execute(Unit)
    }

    // ----------------------------------------------------------------
    class ExzeptionError : BaseUseCase<Unit, Unit>() {
        override suspend fun onRunning(param: Unit) {
            throw Exzeption()
        }
    }

    class ExceptionError : BaseUseCase<Unit, Unit>() {
        override suspend fun onRunning(param: Unit) {
            throw Exception()
        }
    }

    class SecurityExceptionError : BaseUseCase<Unit, Unit>() {
        override suspend fun onRunning(param: Unit) {
            throw SecurityException()
        }
    }

    class TestInputEqualsOutput : BaseUseCase<String, String>() {
        override suspend fun onRunning(param: String): String {
            delay(1000)
            return "Output: $param"
        }
    }

    class TestTimeOut : BaseUseCase<Unit, Unit>() {

        init {
            setTimeOutMillis(1000)
        }

        override suspend fun onRunning(param: Unit) {
            loading()
        }

        suspend fun loading() {
            while (true) {
                delay(1)
            }
        }
    }

    class CallbackInvocation : (Rezult<String, Throwable>) -> Unit {
        var onFailure = mock<(Rezult.Failure<Throwable>) -> Unit>()
        var onSuccess = mock<(Rezult.Success<String>) -> Unit>()

        override fun invoke(result: Rezult<String, Throwable>) {
            when (result) {
                is Rezult.Failure -> {
                    onFailure(result)

                    verify(onFailure, times(1)).invoke(any<Rezult.Failure<Throwable>>())
                }

                is Rezult.Success -> {
                    onSuccess(result)

                    verify(onSuccess, times(1)).invoke(any<Rezult.Success<String>>())
                }
            }
        }
    }
}