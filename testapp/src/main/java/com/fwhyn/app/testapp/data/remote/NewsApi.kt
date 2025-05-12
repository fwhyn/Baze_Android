package com.fwhyn.app.testapp.data.remote

import com.fwhyn.app.testapp.data.model.NewsDto

interface NewsApi {
    suspend fun getLatestNews(): List<NewsDto>
}