package com.fwhyn.domain.usecase

import MainDispatcherRule
import android.util.Log
import com.fwhyn.data.helper.Util
import com.fwhyn.data.helper.extension.getTestTag
import com.fwhyn.data.model.Exzeption
import com.fwhyn.domain.helper.Rezult
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
    fun outputShouldCorrespondsTheInput() = runTest {
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
            .executeOnBackground(input, scope)
    }

    @Test
    fun resultAndExecutionInvocation() = runTest {
        val testInputEqualsOutput = mock<TestInputEqualsOutput>()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testInputEqualsOutput.setResultNotifier(callback)
        verify(testInputEqualsOutput, times(1)).setResultNotifier(eq(callback))

        testInputEqualsOutput.executeOnBackground(input, scope)
        verify(testInputEqualsOutput, times(1)).executeOnBackground(eq(input), eq(scope))
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
            .executeOnBackground(input, scope)
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
            .executeOnBackground(Unit, scope)
    }

    // ----------------------------------------------------------------
    class TestInputEqualsOutput : BaseUseCase<String, String>() {
        override fun executeOnBackground(param: String, scope: CoroutineScope) {
            runWithResult(scope) {
                delay(1000)
                "Output: $param"
            }
        }
    }

    class TestTimeOut : BaseUseCase<Unit, Unit>() {
        override fun executeOnBackground(param: Unit, scope: CoroutineScope) {
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