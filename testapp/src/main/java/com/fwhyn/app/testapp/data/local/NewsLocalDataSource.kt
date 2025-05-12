package com.fwhyn.app.testapp.data.local

import com.fwhyn.app.testapp.data.model.NewsDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsLocalDataSource @Inject constructor() {
    var latestNews: List<NewsDto> = listOf()
        set(value) {
            field = value.map { news ->
                news.copy(title = "${news.title} (Offline News)")
            }
        }
}