package com.example.moviesapp.pages.search_page

import com.example.moviesapp.data.Movies


sealed interface SearchUiState {
    data class Success(val movies: Movies): SearchUiState
    data object Empty: SearchUiState
    data object Searching: SearchUiState
    data object NoResults: SearchUiState
    data object NoInternet: SearchUiState
}