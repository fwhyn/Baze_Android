package com.fwhyn.app.deandro.feature.presentation.user

import MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class UserViewModel2Test {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadUser_updateUiState() = runTest {
        val repository = FakeUserRepo()
        val viewModel = UserViewModel2(repository)

        viewModel.loadUser()
        runCurrent()
//        advanceUntilIdle()

        assertEquals(
            UserUiState.Success("Yana"),
            viewModel.uiState.value
        )
    }

}