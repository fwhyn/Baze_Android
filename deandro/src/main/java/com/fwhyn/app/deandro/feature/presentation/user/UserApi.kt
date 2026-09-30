package com.fwhyn.app.deandro.feature.presentation.user

class UserApi {
    suspend fun getPosts(): List<Post> {
        return listOf(
            Post("asdjkfa"),
            Post("jangan")
        )
    }

    suspend fun getUser(): User {
        return User(
            username = "yana",
            email = "yanawahyuna@gmail.com"
        )
    }
}