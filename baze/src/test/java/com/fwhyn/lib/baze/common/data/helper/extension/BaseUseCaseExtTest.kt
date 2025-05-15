package com.fwhyn.lib.baze.common.data.helper.extension

import MainDispatcherRule
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
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

    val inputForSharedFlow = listOf("DataData 1", "DataData 2")

    @Test
    fun getSharedFlowTest() = runTest {
        val delayMillis = 100L
        val useCase = FlowTest(delayMillis).setWorkerContext(coroutineContext)

        val flowData = useCase.getStateFlowResult(
            scope = backgroundScope,
            started = WhileSubscribed(),
            initialValue = Rezult.Success(initData),
        )
        useCase.execute(param = inputForSharedFlow, scope = this)

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
}