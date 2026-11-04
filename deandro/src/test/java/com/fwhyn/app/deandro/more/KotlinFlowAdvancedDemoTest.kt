package com.fwhyn.app.deandro.more

import android.util.Log
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import org.junit.Test

class KotlinFlowAdvancedDemoTest {
    val tag = "KotlinFlowAdvancedDemoTest"

    @Test
    fun `cold flow test`() = runBlocking {
        val coldFlow = flow {
            Log.d(tag, "STARTED")
            emit(1)
            emit(2)
            emit(3)
            emit(3)
        }

        Log.d(tag, "Collector pertama")
        coldFlow.collect {
            Log.d(tag, "CollectedA: $it")
        }

        Log.d(tag, "Collector kedua")
        coldFlow.collect {
            Log.d(tag, "CollectedB: $it")
        }
    }

    @Test
    fun `flow operator demo`(): Unit = runBlocking {
        flowOf(1, 2, 3, 4, 5, 6)
            .filter { it % 2 == 0 }
            .also { Log.d(tag, "filter even only") }
            .map { it * 10 }
            .also { Log.d(tag, "map multiplied 10") }
            .transform {
                emit(it + 5)
                emit(it + 2)
            }
            .also { Log.d(tag, "transform + 5 ") }
            .take(2)
            .collect { Log.d(tag, "result: $it") }
    }

    @Test
    fun `flow operator drop`(): Unit = runBlocking {
        flowOf(1, 2, 3, 4, 5, 6)
            .drop(2)
            .collect { Log.d(tag, "result: $it") }
    }

    @Test
    fun `flow operator distinct`(): Unit = runBlocking {
        flowOf(1, 1, 2, 3, 4, 4, 4, 5, 1, 6)
            .distinctUntilChanged()
            .collect { Log.d(tag, "result: $it") }
    }
}