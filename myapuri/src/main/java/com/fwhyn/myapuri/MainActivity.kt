package com.fwhyn.myapuri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fwhyn.myapuri.ui.theme.BazeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: MainViewModel by viewModels()

        setContent {
            BazeTheme {
                MainView(
                    textInput = "Android",
                    result = vm.result
                ) {
                    vm.calculateXy(vm.result, 2)
                }
            }
        }
    }
}

@Composable
fun MainView(
    textInput: String,
    result: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Greeting("Yana")
            Spacer(Modifier.width(5.dp))
            Greeting(textInput)
        }

        Spacer(Modifier.height(10.dp))

        Text("Result: $result")

        Spacer(Modifier.weight(1f))

        Button({
            onClick()
        }) {
            Text("Button 1")
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val result by remember { mutableIntStateOf(0) }
    BazeTheme {
        MainView("Android", result = result) { }
    }
}