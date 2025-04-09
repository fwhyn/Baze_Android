package com.fwhyn.baze.domain.usecase

import MainDispatcherRule
import android.util.Log
import com.fwhyn.baze.data.helper.Util
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.data.model.Exzeption
import com.fwhyn.baze.domain.helper.Rezult
import kotlinx.coroutines.CoroutineScope
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

class BaseUseCaseTest {

    val testTag = BaseUseCaseTest::class.java.getTestTag()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val input = "Yana"
    private val outputSuccess = "Output: Yana"
    private val outputFailed = "outputFailed"

    private val results: ArrayList<Rezult<String, Exzeption>> = arrayListOf()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

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
    fun outputShouldCorrespondTheInput() = runTest {
        val scope = this

        val testInputEqualsOutput = TestInputEqualsOutput()
        val callback = { result: Rezult<String, Exception> ->

            when (result) {
                is Rezult.Failure -> {
                    print(result.err.message)
                    Util.throwTestMustNotFailed()
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
    fun callbackInvocation() = runTest {
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
    fun timeoutTest() = runTest {
        val testTimeOut = TestTimeOut()
        val scope = this

        testTimeOut
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err.throwable
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
    fun getResultInBackgroundTest() = runTest {
        val scope = this
        val testInputEqualsOutput = TestInputEqualsOutput()

        val output = testInputEqualsOutput
            .setWorkerContext(coroutineContext)
            .getResult(input, scope)

        Assert.assertEquals("Output: $input", output)
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
                        Util.throwTestMustNotFailed()
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
    fun throwableErrorTest() = runTest {
        val throwableError = ThrowableError()
        val scope = this

        throwableError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Throwable)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .setWorkerContext(coroutineContext)
            .execute(Unit, scope)

        throwableError
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        val theError = it.err
                        Assert.assertTrue(theError is Throwable)
                    }

                    is Rezult.Success -> Util.throwMustNotSuccess()
                }
            }
            .execute(Unit)
    }

    // ----------------------------------------------------------------
    class ExzeptionError : BaseUseCase<Unit, Unit>() {
        override fun execute(param: Unit, scope: CoroutineScope) {
            run(scope) {
                throw Exzeption()
            }
        }
    }

    class ExceptionError : BaseUseCase<Unit, Unit>() {
        override fun execute(param: Unit, scope: CoroutineScope) {
            run(scope) {
                throw Exception()
            }
        }
    }

    class ThrowableError : BaseUseCase<Unit, Unit>() {
        override fun execute(param: Unit, scope: CoroutineScope) {
            run(scope) {
                throw Throwable()
            }
        }
    }

    class TestInputEqualsOutput : BaseUseCase<String, String>() {
        override fun execute(param: String, scope: CoroutineScope) {
            runWithResult(scope) {
                delay(1000)
                "Output: $param"
            }
        }
    }

    class TestTimeOut : BaseUseCase<Unit, Unit>() {
        override fun execute(param: Unit, scope: CoroutineScope) {
            setTimeOut(1000)
            runWithResult(scope) {
                loading()
            }
        }

        suspend fun loading() {
            while (true) {
                delay(1)
            }
        }
    }

    class CallbackInvocation : (Rezult<String, Exzeption>) -> Unit {
        var onFailure = mock<(Rezult.Failure<Exzeption>) -> Unit>()
        var onSuccess = mock<(Rezult.Success<String>) -> Unit>()

        override fun invoke(result: Rezult<String, Exzeption>) {
            when (result) {
                is Rezult.Failure -> {
                    onFailure(result)

                    verify(onFailure, times(1)).invoke(any<Rezult.Failure<Exzeption>>())
                }

                is Rezult.Success -> {
                    onSuccess(result)

                    verify(onSuccess, times(1)).invoke(any<Rezult.Success<String>>())
                }
            }
        }
    }
}