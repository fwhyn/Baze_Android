package com.fwhyn.app.testapp.data.remote

import android.util.Log
import com.fwhyn.app.testapp.data.model.NewsDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
class NewsRemoteDataSource @Inject constructor(
    private val newsApi: NewsApi,
) {
    fun getLatestNews(isOnline: Boolean): Flow<List<NewsDto>> = flow {
        while (isOnline) {
            val job = coroutineContext.job.toString()
            Log.d("NewsRemoteDataSource", "Fetching latest news job: $job")

            val latestNews = newsApi.getLatestNews()
            emit(latestNews)
            delay(1000)
        }
    }
}