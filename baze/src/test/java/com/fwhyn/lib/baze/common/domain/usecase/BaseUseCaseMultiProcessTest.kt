package com.fwhyn.lib.baze.common.domain.usecase

import MainDispatcherRule
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseUseCaseMultiProcessTest {

    val testTag = BaseUseCaseMultiProcessTest::class.java.getDebugTag()

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
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        val job1 = TestInputEqualsOutput()
        val job2 = TestInputEqualsOutput()

        job1.setResultNotifier { result ->
            if (result is Rezult.Success) results.add(result)
        }.setWorkerContext(coroutineContext).execute("Job1", scope)

        job2.setResultNotifier { result ->
            if (result is Rezult.Success) results.add(result)
        }.setWorkerContext(coroutineContext).execute("Job2", scope)

        job1.join()
        job2.join()

        Assert.assertEquals(2, results.size)
        Assert.assertEquals("Output: Job1", (results[0] as Rezult.Success<String>).dat)
        Assert.assertEquals("Output: Job2", (results[1] as Rezult.Success<String>).dat)
    }

    @Test
    fun cancelSpecificJobTest() = runTest {
        val scope = this
        val job1 = TestInputEqualsOutput()
        val job2 = TestInputEqualsOutput()

        job1.setWorkerContext(coroutineContext).execute("Job1", scope)
        job2.setWorkerContext(coroutineContext).execute("Job2", scope)

        job1.cancelPreviousActiveJob()

        val job1Id = job1.getId()
        val job2Id = job2.getId()

        Assert.assertNotEquals(job1Id, job2Id)
        Assert.assertTrue(job1Id.isNotEmpty())
        Assert.assertTrue(job2Id.isNotEmpty())
    }

    @Test
    fun lifecycleEventsForMultipleJobsTest() = runTest {
        val scope = this
        val job1 = TestInputEqualsOutput()
        val job2 = TestInputEqualsOutput()

        val job1Events: ArrayList<BaseUseCase.LifeCycle> = arrayListOf()
        val job2Events: ArrayList<BaseUseCase.LifeCycle> = arrayListOf()

        job1.setLifeCycleNotifier { event -> job1Events.add(event) }
            .setWorkerContext(coroutineContext)
            .execute("Job1", scope)

        job2.setLifeCycleNotifier { event -> job2Events.add(event) }
            .setWorkerContext(coroutineContext)
            .execute("Job2", scope)

        job1.join()
        job2.join()

        Assert.assertEquals(listOf(BaseUseCase.LifeCycle.OnStart, BaseUseCase.LifeCycle.OnFinish), job1Events)
        Assert.assertEquals(listOf(BaseUseCase.LifeCycle.OnStart, BaseUseCase.LifeCycle.OnFinish), job2Events)
    }

    @Test
    fun executeTwoProcessesInSameObjectTest() = runTest {
        val testInputEqualsOutput = TestInputEqualsOutput()
        val scope = this
        val results: ArrayList<Rezult<String, Throwable>> = arrayListOf()

        // Execute the first process
        testInputEqualsOutput
            .setResultNotifier { result ->
                if (result is Rezult.Success) results.add(result)
            }
            .setWorkerContext(coroutineContext)
            .execute("FirstProcess", scope)

        val firstId = testInputEqualsOutput.getId()
        Assert.assertTrue(firstId.isNotEmpty())

        // Execute the second process
        testInputEqualsOutput.execute("SecondProcess", scope)

        val secondId = testInputEqualsOutput.getId()
        Assert.assertTrue(secondId.isNotEmpty())
        Assert.assertNotEquals(firstId, secondId)

        // Wait for the second process to complete
        testInputEqualsOutput.join()

        // Validate the result corresponds to the second process
        Assert.assertEquals(1, results.size)
        Assert.assertEquals("Output: SecondProcess", (results[0] as Rezult.Success<String>).dat)
    }

    // ----------------------------------------------------------------
    class TestInputEqualsOutput : BaseUseCase<String, String>() {
        override suspend fun onRunning(param: String): String {
            delay(1000)
            return "Output: $param"
        }
    }
}