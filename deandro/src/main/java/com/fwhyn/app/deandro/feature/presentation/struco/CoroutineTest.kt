package com.fwhyn.app.deandro.feature.presentation.struco

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class TimAppli(
    val nama: String,
    val NIK: String,
    val riwayat: List<String>
)

class CoroutineTest : ViewModel() {
    private val _stateTimAppli = MutableStateFlow<TimAppli?>(null)
    val stateTimAppli: StateFlow<TimAppli?> = _stateTimAppli.asStateFlow()

    init {
        siapaSajaTimAppli()
    }

    private fun siapaSajaTimAppli() {
        viewModelScope.launch {
            val namaMember = async {
                delay(2000.milliseconds)
                "Tono"
            }

            val memberNik = async {
                delay(5000.milliseconds)
                "232498"
            }

            val riwayat = async {
                delay(2000.milliseconds)
                listOf("absen", "asdf", "asdf")
            }

            val hasil = TimAppli(
                nama = namaMember.await(),
                NIK = memberNik.await(),
                riwayat = riwayat.await()
            )

            _stateTimAppli.value = hasil
        }
    }
}