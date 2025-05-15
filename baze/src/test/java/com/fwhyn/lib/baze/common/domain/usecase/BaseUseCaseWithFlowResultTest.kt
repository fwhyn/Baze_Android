package com.fwhyn.lib.baze.common.domain.usecase

import MainDispatcherRule
import android.util.Log
import com.fwhyn.lib.baze.common.data.helper.Util
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import com.fwhyn.lib.baze.common.data.helper.extension.getFlowResult
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseUseCaseWithFlowResultTest {

    val testTag = BaseUseCaseWithFlowResultTest::class.java.getDebugTag()

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

        val firstData = flowData.first() as? Rezult.Success
        val output = flowData.toList().map {
            (it as Rezult.Success).dat
        }

        flowData.collect {
            when (it) {
                is Rezult.Failure<Throwable> -> Util.throwMustNotFailed()
                is Rezult.Success<String> -> Log.d(testTag, it.dat)
            }
        }

        Assert.assertEquals(input, output)
        Assert.assertEquals(input[0], firstData?.dat)
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