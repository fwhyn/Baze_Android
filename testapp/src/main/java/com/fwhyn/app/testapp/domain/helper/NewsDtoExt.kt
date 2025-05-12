package com.fwhyn.app.testapp.domain.helper

import com.fwhyn.app.testapp.data.model.NewsDto
import com.fwhyn.app.testapp.domain.model.News


fun NewsDto.toNews() = News(
    title = title,
    description = description,
    imageUrl = imageUrl,
    date = date,
    link = link
)