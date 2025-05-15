package com.fwhyn.lib.baze.common.data.helper.extension

import MainDispatcherRule
import app.cash.turbine.test
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseUseCaseExtTest {

    val testTag = BaseUseCaseExtTest::class.java.getDebugTag()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // ----------------------------------------------------------------
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // ----------------------------------------------------------------

    val input = listOf("data 1", "data 2", "data 3", "data 4")

    @Test
    fun getFlowTest() = runTest {
        val useCase = FlowTest().setWorkerContext(coroutineContext)

        val flowData = useCase.getFlowResult()
        useCase.execute(param = input, scope = this)

        // Get all emitted values
        val emittedValues = flowData.take(input.size).toList()

        // Check the stream values
        for (i in input.indices) {
            val data = emittedValues[i] as? Rezult.Success
            Assert.assertEquals(input[i], data?.dat)
        }
    }

    val initData = "initial data"

    @Test
    fun getStateFlowTest() = runTest {
        val delayMillis = 100L
        val useCase = FlowTest(delayMillis).setWorkerContext(coroutineContext)

        val flowData = useCase.getStateFlowResult(initialValue = Rezult.Success(initData))
        useCase.execute(param = input, scope = this)

        // Get all emitted values including + the initial value
        val emittedValues = flowData.take(input.size + 1).toList()

        // Check the initial value
        val initDataResult = emittedValues[0] as? Rezult.Success
        Assert.assertEquals(initData, initDataResult?.dat)

        // Check the stream values
        for (i in input.indices) {
            val data = emittedValues[i + 1] as? Rezult.Success
            Assert.assertEquals(input[i], data?.dat)
        }
    }

    class FlowTest(val delayMillis: Long = 100) : BaseUseCase<List<String>, String>() {
        override suspend fun onRunning(param: List<String>): String {
            val size = param.size - 1

            // loop until second last item
            for (i in 0..size - 1) {
                delay(delayMillis)
                notifyResult(Rezult.Success(param[i]))
            }

            // return the last item
            return param[size]
        }
    }

    @Test
    fun getSharedFlowTest() = runTest {
        val scope = this
        val delayMillis = 10L
        val useCase = SharedFlowTest(delayMillis).setWorkerContext(coroutineContext)
        val replay = 2

        val flowData2 = useCase.getSharedFlowResult(replay = replay)

        // Collect the flow using collect
        val size = 3
        for (i in 0..size - 1) {
            useCase.execute(param = input[i], scope = scope)
            delay(100)
        }

        val dataResults2 = flowData2.replayCache
        Assert.assertEquals(replay, dataResults2.size)

        // Wait for the flow to emit values
        for (i in dataResults2.indices) {
            val index = i + size - replay
            val dataResult = dataResults2[i] as? Rezult.Success
            Assert.assertEquals(input[index], dataResult?.dat)
        }

        // Collect the flow using turbine
        flowData2.test {
            for (i in 0..replay - 1) {
                val index = i + size - replay
                val dataResult = awaitItem() as? Rezult.Success
                Assert.assertEquals(input[index], dataResult?.dat)
            }
        }
    }

    class SharedFlowTest(val delayMillis: Long = 100) : BaseUseCase<String, String>() {
        override suspend fun onRunning(param: String): String {
            delay(delayMillis)
            return param
        }
    }

    @Test
    fun `test SharedFlow with replay`() = runTest {
        val replayCount = 2
        val sharedFlow = MutableSharedFlow<Int>(replayCount)

        // Emit some values
        sharedFlow.emit(1)
        sharedFlow.emit(2)
        sharedFlow.emit(3)

        // Collect values and assert
        val collectedValues = sharedFlow.replayCache

        // Ensure that the last 'replayCount' values are received
        Assert.assertEquals(listOf(2, 3), collectedValues)
    }
}