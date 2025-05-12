package com.fwhyn.app.testapp.data.repository

import android.util.Log
import com.fwhyn.app.testapp.data.helper.NetworkMonitor
import com.fwhyn.app.testapp.data.local.NewsLocalDataSource
import com.fwhyn.app.testapp.data.model.NewsDto
import com.fwhyn.app.testapp.data.remote.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val networkMonitor: NetworkMonitor,
) : NewsRepository {

    override fun getLatestNews(): Flow<List<NewsDto>> = flow {
        newsLocalDataSource.latestNews

        networkMonitor.isOnline.collect {
            Log.d("NewsRepositoryImpl", "Network status: $it")

            newsRemoteDataSource.getLatestNews(it).collect { latestNews ->
                newsLocalDataSource.latestNews = latestNews
                emit(latestNews)
            }

            if (!it) {
                Log.d("NewsRepositoryImpl", "Offline news")
                emit(newsLocalDataSource.latestNews)
            }
        }
    }
}