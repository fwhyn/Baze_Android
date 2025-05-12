package com.fwhyn.app.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fwhyn.app.testapp.domain.helper.Result
import com.fwhyn.app.testapp.domain.model.News
import com.fwhyn.app.testapp.ui.theme.BazeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BazeTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val vm = hiltViewModel<MainViewModel>()
    val newsList by vm.newsList.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Top Bar")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.DarkGray,
                    actionIconContentColor = Color.DarkGray,
                    navigationIconContentColor = Color.DarkGray
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.LightGray,
                contentColor = Color.DarkGray
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Bottom Bar"
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding() + 16.dp,
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                end = innerPadding.calculateStartPadding(LayoutDirection.Rtl) + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            )
        ) {
            Greeting()
            Spacer(Modifier.height(5.dp))
            News(value = newsList)
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Hello, I'm Yana!",
        modifier = modifier
    )
}

@Composable
fun News(
    modifier: Modifier = Modifier,
    value: Result<List<News>>
) {
    when (value) {
        is Result.Loading -> {
            Text(
                text = "Loading...",
                modifier = modifier.padding(16.dp)
            )
        }

        is Result.Success -> {
            LazyColumn(modifier = modifier) {
                items(value.data) { newsItem ->
                    NewsItem(news = newsItem)
                }
            }
        }

        is Result.Error -> {
            Text(
                text = "Error: ${value.error.message}",
                modifier = modifier.padding(16.dp),
                color = Color.Red
            )
        }
    }
}

@Composable
fun NewsItem(news: News) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = news.title)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = news.description)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BazeTheme {
        MainScreen()
    }
}