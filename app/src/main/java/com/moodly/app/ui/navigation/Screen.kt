package com.moodly.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Register : Screen("register")
    object History : Screen("history")
    object Detail : Screen("detail/{entryId}") {
        fun createRoute(id: Long) = "detail/$id"
    }
    object Resources : Screen("resources")
}
