package com.fwhyn.app.testapp.data.remote

import com.fwhyn.app.testapp.data.model.NewsDto
import kotlin.random.Random

class NewsApiImpl : NewsApi {
    override suspend fun getLatestNews(): List<NewsDto> {
        return List(10) { index ->
            NewsDto(
                id = "news_$index",
                title = "Random Title ${Random.nextInt(1000)}",
                description = "Random Description ${Random.nextInt(1000)}",
                imageUrl = "https://via.placeholder.com/150?text=Image+${Random.nextInt(1000)}",
                date = "2025-06-${Random.nextInt(1, 28).toString().padStart(2, '0')}",
                link = "https://example.com/news/${Random.nextInt(1000)}"
            )
        }
    }
}