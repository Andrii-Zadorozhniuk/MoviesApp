package com.example.moviesapp.pages.profile_page

import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.data.entities.WatchHistoryEntity
import com.example.moviesapp.pages.home_page.MoviesHomeState

data class ProfileState(
    val likedMovies: List<LikedMovieEntity> = emptyList(),
    val watchHistory: List<WatchHistoryEntity> = emptyList(),
)

sealed interface ProfileUiState {
    data class Success(val profile: ProfileState): ProfileUiState
    data object Error: ProfileUiState
    data object Loading: ProfileUiState
}