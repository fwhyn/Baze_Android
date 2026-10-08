package com.fwhyn.app.deandro.feature.presentation.struco

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LearnAndroid(viewModel: CoroutineTest = CoroutineTest()) {
    val tabelData by viewModel.stateTimAppli.collectAsStateWithLifecycle()

    if (tabelData == null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val data = tabelData!!
        Column {
            Text(text = "Nama: ${data.nama}")
            Text(text = "NIK: ${data.NIK}")
            Text(text = "Riwayat: ${data.riwayat}")
        }
    }
}

fun cek() {
    var tabelData: TimAppli? = null

    tabelData = TimAppli("asd", "sdaf", listOf())

    if (tabelData == null) {
        Log.d("sadf", "asdf")
    } else {
        val data = tabelData
    }
}