package com.fwhyn.app.testapp.domain.usecase

import android.util.Log
import com.fwhyn.app.testapp.data.model.NewsDto
import com.fwhyn.app.testapp.data.repository.NewsRepository
import com.fwhyn.app.testapp.domain.helper.Result
import com.fwhyn.app.testapp.domain.helper.toNews
import com.fwhyn.app.testapp.domain.model.GetNewsParam
import com.fwhyn.app.testapp.domain.model.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetNewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository,
) : GetNewsUseCase {

    private var currentJob: Job? = null

    override fun execute(
        scope: CoroutineScope,
        param: GetNewsParam,
    ): StateFlow<Result<List<News>>> {
        currentJob?.cancel()

        val newJob = Job()
        currentJob = newJob
        Log.d("GetNewsUseCaseImpl", "Current Job: $currentJob")

        val newListDto: Flow<List<NewsDto>> = newsRepository.getLatestNews()
        val newListNews: Flow<List<News>> = newListDto.map { newsList ->
            newsList.map { newsDto ->
                newsDto.toNews()
            }
        }

        val filteredNews: Flow<List<News>> = if (param.keyword != "") {
            newListNews.map { newsList ->
                newsList.filter {
                    it.title.contains(param.keyword, ignoreCase = true)
                }
            }
        } else {
            newListNews
        }

        return filteredNews
            .map { newsList ->
                try {
                    Result.Success(newsList)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .stateIn(
                scope = CoroutineScope(scope.coroutineContext + newJob),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Result.Loading,
            )
    }
}