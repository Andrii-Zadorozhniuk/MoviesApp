package com.example.moviesapp.pages.search_page

import android.app.Application
import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviesapp.data.ConnectivityRepository
import com.example.moviesapp.data.ConnectivityState
import com.example.moviesapp.data.DefaultConnectivityRepository
import com.example.moviesapp.data.Movies
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.MoviesRepositoryImpl
import com.example.moviesapp.pages.home_page.MoviesHomeUiState
import com.example.moviesapp.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
): ViewModel() {
    var uiState: SearchUiState by mutableStateOf(SearchUiState.Empty)
    var searchQuery by mutableStateOf("")
    fun fetchData() {
        viewModelScope.launch {
            connectivityRepository.connectivityState.collect { netState ->
                if (netState == ConnectivityState.Available) {
                    uiState = try {
                        val foundMovies = searchMovies(searchQuery)
                        if (foundMovies.results?.isEmpty() == true) {
                            SearchUiState.NoResults
                        } else {
                            SearchUiState.Success(foundMovies)
                        }
                    } catch (e: Exception) {
                        SearchUiState.NoResults
                    }
                } else {
                    uiState = SearchUiState.NoInternet
                }
            }

        }
    }


    fun onQueryChanged(newQuery: String) {
        uiState = SearchUiState.Searching
        if (newQuery.isBlank()) {
            uiState = SearchUiState.Empty
            searchQuery = ""
        }
        else {
            searchQuery = newQuery
            fetchData()
        }

    }

    private suspend fun searchMovies(query: String): Movies {
        val endUrl = "search/movie?api_key=$API_KEY&query=$query"
        return moviesRepository.searchMovies(endUrl)
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as Application)
//                val connectivityManager =
//                val apiService = MoviesApi.retrofitService
//                val moviesRepository = MoviesRepositoryImpl(apiService)
//                SearchViewModel(moviesRepository, )
//            }
//        }
//    }
}