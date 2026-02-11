package com.fwhyn.app.deandro.feature.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class CalculatorViewModel : ViewModel() {

    private val plus = Plus()

    val result: MutableStateFlow<Double> = MutableStateFlow(0.0)

    fun onPlusClicked(a: Double, b: Double) {
        val _result = plus(a, b)
        result.value = _result
    }
}