package com.fwhyn.app.testapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.app.testapp.domain.helper.Result
import com.fwhyn.app.testapp.domain.model.GetNewsParam
import com.fwhyn.app.testapp.domain.model.News
import com.fwhyn.app.testapp.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    var newsList: StateFlow<Result<List<News>>> = getNewsUseCase.execute(viewModelScope, GetNewsParam(""))
        private set

    fun onSearchNews(keyword: String) {
        newsList = getNewsUseCase.execute(viewModelScope, GetNewsParam(keyword))
    }
}