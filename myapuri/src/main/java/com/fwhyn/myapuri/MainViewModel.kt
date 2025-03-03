package com.fwhyn.myapuri

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val calculateXyModel = CalculateXyModel()

    var result by mutableIntStateOf(0)
        private set

    fun calculateXy(x: Int, y: Int) {
        result = calculateXyModel.calculateXy(x, y)
    }
}