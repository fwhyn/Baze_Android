package com.fwhyn.app.deandro.feature.presentation.struco

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.milliseconds

data class TimAppli(
    val nama: String,
    val NIK: String,
    val riwayat: List<String>
)

class CoroutineTestViewModel : ViewModel() {
    private val _stateTimAppli = MutableStateFlow<TimAppli?>(null)
    val stateTimAppli: StateFlow<TimAppli?> = _stateTimAppli.asStateFlow()

    init {
        siapaSajaTimAppli()
    }

    private fun siapaSajaTimAppli() {
        viewModelScope.launch {
            supervisorScope {
                val namaMember = async {
                    delay(2000.milliseconds)
                    throw Exception("gagal baca")

                    val result = "Yana"
                    Log.d("CoroutineTestViewModel", "result: $result")

                    result
                }.apply {
                    invokeOnCompletion { cause ->
                        Log.e("CoroutineTestViewModel", cause?.message.toString())
                    }
                }

                val memberNik = async {
                    delay(5000.milliseconds)

                    val result = "232498"
                    Log.d("CoroutineTestViewModel", "result: $result")

                    result
                }.apply {
                    invokeOnCompletion { cause ->
                        Log.e("CoroutineTestViewModel", cause?.message.toString())
                    }
                }

                val riwayat = async {
                    delay(2000.milliseconds)

                    val result = listOf("absen", "asdf", "asdf")
                    Log.d("CoroutineTestViewModel", "result: $result")

                    result
                }.apply {
                    invokeOnCompletion { cause ->
                        Log.e("CoroutineTestViewModel", cause?.message.toString())
                    }
                }

                try {
                    _stateTimAppli.value = TimAppli(
                        nama = namaMember.await(),
                        NIK = memberNik.await(),
                        riwayat = riwayat.await()
                    )
                } catch (e: Exception) {
                    _stateTimAppli.value = TimAppli(
                        nama = e.message.toString(),
                        NIK = "",
                        riwayat = listOf()
                    )
                }
            }
        }
    }
}