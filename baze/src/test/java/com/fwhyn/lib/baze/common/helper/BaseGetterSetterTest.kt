package com.fwhyn.lib.baze.common.helper

import MainDispatcherRule
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseGetterSetterTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTest() = runTest {
        val dataSource = DataSource()

        val data1 = dataSource.get(Unit)
        Assert.assertEquals(1, data1.value)

        val data2 = dataSource.get(Unit)
        Assert.assertEquals(1, data2.value)

        val data3 = dataSource.get(Unit)
        Assert.assertEquals(1, data3.value)
    }

    @Test
    fun executeUseCaseTest() = runTest {
        val dataSource = DataSource()
        val getDataUseCase = GetDataUseCase(dataSource)

        repeat(2) {
            getDataUseCase
                .setWorkerContext(coroutineContext)
                .invoke(
                    scope = this,
                    onFetchParam = { },
                    onOmitResult = {
                        it.onSuccess { data ->
                            Assert.assertEquals(1, data.value)
                        }.onFailure {
                            throw Exception("Must not failed")
                        }
                    }
                )
        }
    }

    // ----------------------------------------------------------------
    class GetDataUseCase(private val dataSource: DataSource) : BaseRunner<Unit, WeightData>() {

        override suspend fun onRunning(param: Unit, result: suspend (WeightData) -> Unit) {
            val res = dataSource.get(param)
            result(res)
        }
    }

    class DataSource : BaseGetter<Unit, WeightData>, BaseSetter<Unit, WeightData> {

        override suspend fun get(param: Unit): WeightData = suspendCancellableCoroutine { continuation ->
            var isParsingData = false

            WithCallback().start(
                onResult = {
                    isParsingData = true

                    if (continuation.isActive) {
                        continuation.resume(getWeightData(it)) { cause, _, _ ->
                            throw Exception(cause)
                        }
                    }
                },
                onFinished = {
                    if (!isParsingData && continuation.isActive) {
                        continuation.resume(WeightData(0)) { cause, _, _ ->
                            throw Exception(cause)
                        }
                    }
                }
            )
        }

        override suspend fun set(param: Unit, data: WeightData) {
            TODO("Not yet implemented")
        }

        private fun getWeightData(it: Int): WeightData {
            var result = 0

            Thread {
                for (i in 0..1000) {
                    for (i in 0..1000) {
                        for (i in 0..1000) {
                            // do nothing
                        }
                    }
                    result++
                }
            }.start()

            return WeightData(1)
        }
    }

    class WithCallback {
        fun start(onResult: (Int) -> Unit, onFinished: () -> Unit) {
            var result = 0
            onResult(result)

            Thread {
                for (i in 0..1000) {
                    for (i in 0..1000) {
                        for (i in 0..1000) {
                            // do nothing
                        }
                    }
                    result++
                }
            }.start()

            onFinished()
        }
    }

    data class WeightData(val value: Int)
}

