package com.fwhyn.myapuri

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculateXyModel: Calculate<CalculateXyModel.Input, Int>
) : ViewModel() {

    var result by mutableIntStateOf(0)
        private set

    fun calculateXy(x: Int, y: Int) {
        result = calculateXyModel.calculate(CalculateXyModel.Input(x, y))
    }
}