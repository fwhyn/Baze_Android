package com.fwhyn.app.deandro.feature.presentation.user

class FakeUserRepo : UserRepo {

    override suspend fun getUserName(): String {
        return "Yana"
    }
}