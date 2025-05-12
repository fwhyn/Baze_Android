package com.fwhyn.app.testapp.data.repository

import com.fwhyn.app.testapp.data.model.NewsDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getLatestNews(): Flow<List<NewsDto>>
}