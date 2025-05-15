package com.fwhyn.lib.baze.common.data.helper.extension

import MainDispatcherRule
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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

        for (i in input.indices) {
            val data = flowData.first() as? Rezult.Success
            Assert.assertEquals(input[i], data?.dat)
        }
    }

    val initData = "initial data"

    @Test
    fun getStateFlowTest() = runTest {
        val delayMillis = 100L
        val useCase = FlowTest(delayMillis).setWorkerContext(coroutineContext)

        val flowData = useCase.getStateFlowResult(
            scope = backgroundScope,
            started = WhileSubscribed(),
            initialValue = Rezult.Success(initData),
        )
        useCase.execute(param = input, scope = this)

        // Get all emitted values including + the initial value
        val emittedValues = flowData.take(input.size + 1).toList()

        // Check the initial value
        val initDataResult = emittedValues[0] as? Rezult.Success
        Assert.assertEquals(initData, initDataResult?.dat)

        // Check the steam values
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
        val delayMillis = 10L
        val useCase = SharedFlowTest(delayMillis).setWorkerContext(coroutineContext)

        val flowData2 = useCase.getSharedFlowResult(
            scope = backgroundScope,
            started = WhileSubscribed(),
            replay = 2,
        )

        val scope = this

        // Collect the flow using collect
        for (i in 0..2) {
            useCase.execute(param = input[i], scope = scope)
            delay(100)
        }

        val dataResults2 = mutableListOf<Rezult<String, Throwable>>()
        val job2 = launch {
            flowData2.collect { dataResult -> dataResults2.add(dataResult) }
        }
        delay(100)

        Assert.assertEquals(2, dataResults2.size)

        // Wait for the flow to emit values
        for (i in dataResults2.indices) {
            val dataResult = dataResults2[i] as? Rezult.Success
            Assert.assertEquals(input[i], dataResult?.dat)
        }

        // Collect the flow using turbine
//        flowData.test {
//            useCase.execute(param = inputForSharedFlow, scope = scope)
//            var dataResult = awaitItem() as? Rezult.Success
//            Assert.assertEquals(inputForSharedFlow, dataResult?.dat)
//
//            useCase.execute(param = "Data 2", scope = scope)
//            dataResult = awaitItem() as? Rezult.Success
//            Assert.assertEquals("Data 2", dataResult?.dat)
//        }
//
//        flowData2.test {
//            useCase.execute(param = inputForSharedFlow, scope = scope)
//            val dataResult = awaitItem() as? Rezult.Success
//            Assert.assertEquals(inputForSharedFlow, dataResult?.dat)
//        }

        job2.cancel()
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
        val collectedValues = mutableListOf<Int>()
        val collectJob = launch {
            sharedFlow.collect {
                collectedValues.add(it)
            }
        }

        delay(1000)

        // Ensure that the last 'replayCount' values are received
        Assert.assertEquals(listOf(1, 2, 3), collectedValues)

        collectJob.cancel()
    }
}