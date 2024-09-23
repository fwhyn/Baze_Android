package com.fwhyn.domain.usecase

import MainDispatcherRule
import com.fwhyn.data.helper.Util
import com.fwhyn.data.model.Exzeption
import com.fwhyn.domain.helper.Rezult
import kotlinx.coroutines.CoroutineScope
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

        val testUseCase = TestUseCase()
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
        testUseCase
            .setResultNotifier(callback)
            .setWorkerContext(coroutineContext)
            .executeOnBackground(input, scope)
    }

    @Test
    fun resultAndExecutionInvocation() = runTest {
        val testUseCase = mock<TestUseCase>()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testUseCase.setResultNotifier(callback)
        verify(testUseCase, times(1)).setResultNotifier(eq(callback))

        testUseCase.executeOnBackground(input, scope)
        verify(testUseCase, times(1)).executeOnBackground(eq(input), eq(scope))
    }

    @Test
    fun callbackInvocation() = runTest {
        val testUseCase = TestUseCase()
        val scope = this
        val input = "Yana"

        val callback = CallbackInvocation()

        testUseCase
            .setResultNotifier(callback)
            .setWorkerContext(coroutineContext)
            .executeOnBackground(input, scope)
    }

    // ----------------------------------------------------------------
    class TestUseCase : BaseUseCase<String, String>() {
        override fun executeOnBackground(param: String, scope: CoroutineScope) {
            runWithResult(scope) {
                delay(1000)
                "Output: $param"
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