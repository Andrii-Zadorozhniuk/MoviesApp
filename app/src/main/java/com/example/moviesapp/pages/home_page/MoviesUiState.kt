package com.example.moviesapp.pages.home_page

import com.example.moviesapp.data.Movies

data class MoviesHomeState(
    val topRatedMovies: Movies,
    val upcomingMovies: Movies
)

sealed interface MoviesHomeUiState {
    data class Success(val movies: MoviesHomeState): MoviesHomeUiState
    data object Error: MoviesHomeUiState
    data object Loading: MoviesHomeUiState
}