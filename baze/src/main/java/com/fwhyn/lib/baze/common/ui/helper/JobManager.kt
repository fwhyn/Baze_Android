package com.fwhyn.lib.baze.common.ui.helper

import com.fwhyn.lib.baze.common.data.helper.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class JobManager {

    /**
     * Unique identifier for the job.
     */
    var jobId = Util.getUniqueId()
        private set

    /**
     * The context in which the worker will run.
     * Default is Dispatchers.Default, but can be overridden.
     */
    var workerContext: CoroutineContext = Dispatchers.Default

    /**
     * The currently active job.
     * This property is set to null when there is no active job.
     * When a new job is assigned, the previous active job is canceled.
     */
    protected var job: Job? = null
        set(value) {
            cancelPreviousActiveJob()
            jobId = Util.getUniqueId()
            field = value
        }

    // ----------------------------------------------------------------
    /**
     * Cancels the currently active job, if any.
     *
     * @return The current instance of the use case.
     */
    fun cancelPreviousActiveJob(): JobManager {
        if (job?.isActive == true) {
            job?.cancel()
        }

        return this
    }

    /**
     * Waits for the current job to complete.
     */
    suspend fun join() = job?.join()
}