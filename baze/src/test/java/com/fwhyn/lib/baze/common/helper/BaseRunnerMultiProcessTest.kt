package com.fwhyn.lib.baze.common.helper

import MainDispatcherRule
import com.fwhyn.lib.baze.common.helper.extension.getDebugTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseRunnerMultiProcessTest {

    val testTag = BaseRunnerMultiProcessTest::class.java.getDebugTag()

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
    @Test
    fun multipleJobsExecutionTest() = runTest {
        val scope = this
        val results: ArrayList<String> = arrayListOf()

        val job1 = TestInputEqualsOutput()
        val job2 = TestInputEqualsOutput()

        job1
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = scope,
                onGetParam = { "Job1" },
                result = {
                    it.onSuccess { output -> results.add(output) }
                }
            )


        job2
            .setWorkerContext(coroutineContext)
            .invoke(
                scope = scope,
                onGetParam = { "Job2" },
                result = {
                    it.onSuccess { output -> results.add(output) }
                }
            )

        job1.join()
        job2.join()

        Assert.assertEquals(2, results.size)
        Assert.assertEquals("Output: Job1", results[0])
        Assert.assertEquals("Output: Job2", results[1])
    }

    @Test
    fun cancelSpecificJobTest() = runTest {
        val scope = this
        val job1 = TestInputEqualsOutput()
        val job2 = TestInputEqualsOutput()

        job1.setWorkerContext(coroutineContext).invoke(scope, { "Job1" })
        job2.setWorkerContext(coroutineContext).invoke(scope, { "Job2" })

        job1.cancelPreviousActiveJob()

        val job1Id = job1.getId()
        val job2Id = job2.getId()

        Assert.assertNotEquals(job1Id, job2Id)
        Assert.assertTrue(job1Id.isNotEmpty())
        Assert.assertTrue(job2Id.isNotEmpty())
    }

    @Test
    fun executeTwoProcessesInSameObjectTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<String> = arrayListOf()

        testInputEqualsOutput.setWorkerContext(coroutineContext)

        // Execute the first process
        testInputEqualsOutput.invoke(
            scope = scope,
            onGetParam = { "FirstProcess" },
            result = {
                it.onSuccess { output ->
                    results.add(output)
                }
            }
        )

        val firstId = testInputEqualsOutput.getId()
        Assert.assertTrue(firstId.isNotEmpty())

        // Execute the second process
        testInputEqualsOutput.invoke(
            scope = scope,
            onGetParam = { "SecondProcess" },
            result = {
                it.onSuccess { output ->
                    results.add(output)
                }
            }
        )

        val secondId = testInputEqualsOutput.getId()
        Assert.assertTrue(secondId.isNotEmpty())
        Assert.assertNotEquals(firstId, secondId)

        // Wait for the second process to complete
        testInputEqualsOutput.join()

        // Validate the result corresponds to the second process
        Assert.assertEquals(1, results.size)
        Assert.assertEquals("Output: SecondProcess", results[0])
    }

    // ----------------------------------------------------------------
    class TestInputEqualsOutput : BaseRunner<String, String>() {

        override suspend fun onRunning(param: String, result: suspend (String) -> Unit) {
            delay(1000)
            result("Output: $param")
        }
    }
}