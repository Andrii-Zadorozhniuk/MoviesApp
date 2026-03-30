package com.example.moviesapp.destinations

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object Details : Screen("details/{movieId}") {
        fun createRoute(id: Int) = "details/$id"
    }
}