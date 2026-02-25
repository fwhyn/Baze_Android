package com.fwhyn.app.deandro.feature.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CalculatorViewModel : ViewModel() {

    private val plus = Plus()

    private val _result: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val result: StateFlow<Double> = _result

    fun onPlusClicked(a: Double, b: Double) {
        _result.value = plus(a, b)
    }
}