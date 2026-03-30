package com.example.moviesapp.pages.movie_details

import com.example.moviesapp.data.Movies

data class MovieDetailsState(
    val movie: Movies.Movie,
    val similarMovies: Movies,
    //val isLiked: Boolean = false,
)

sealed interface MovieDetailsUiState {
    data class Success(val movieDetailsState: MovieDetailsState): MovieDetailsUiState
    data object Loading: MovieDetailsUiState
    data object Error: MovieDetailsUiState
}