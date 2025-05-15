package com.fwhyn.lib.baze.common.data.helper.extension

import MainDispatcherRule
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    fun getFlowTest() = kotlinx.coroutines.test.runTest {
        val useCase = FlowTest().setWorkerContext(coroutineContext)

        val flowData = useCase.getFlowResult()
        useCase.execute(param = input, scope = this)

        for (i in input.indices) {
            val data = flowData.first() as? Rezult.Success
            Assert.assertEquals(input[i], data?.dat)
        }

//        val data1 = flowData.first() as? Rezult.Success
//        Assert.assertEquals(input[0], data1?.dat)
//
//        val data2 = flowData.first() as? Rezult.Success
//        Assert.assertEquals(input[2], data2?.dat)
    }

    class FlowTest() : BaseUseCase<List<String>, String>() {
        override suspend fun onRunning(param: List<String>): String {
            val size = param.size - 1

            // loop until second last item
            for (i in 0..size - 1) {
                notifyResult(Rezult.Success(param[i]))
                delay(1000)
            }

            // return the last item
            return param[size]
        }
    }
}