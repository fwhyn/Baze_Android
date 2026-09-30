package com.fwhyn.app.deandro.feature.presentation.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    init {
        loadDashboard()
    }

    val dashboardFlow: MutableStateFlow<Dashboard> = MutableStateFlow(
        Dashboard(
            user = User("-", ("-")),
            posts = listOf()
        )
    )

    fun loadDashboard() {
        viewModelScope.launch {

            try {
                val user = async {
                    repository.getUser()
                }

                val posts = async {
                    repository.getPosts()
                }

                val dashboard = Dashboard(
                    user = user.await(),
                    posts = posts.await()
                )

                // Runs on Main by default
                updateUi(dashboard)

            } catch (e: Exception) {
                Log.e("User", "${e.message}")
            }
        }
    }

    fun updateUi(dashboard: Dashboard) {
        dashboardFlow.value = dashboard
    }
}