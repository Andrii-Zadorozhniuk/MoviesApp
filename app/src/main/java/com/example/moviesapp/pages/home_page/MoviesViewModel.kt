package com.example.moviesapp.pages.home_page

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
import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesHomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {
    var uiState: MoviesHomeUiState by mutableStateOf(MoviesHomeUiState.Loading)


    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        uiState = MoviesHomeUiState.Error
    }
    fun fetchData() {
        viewModelScope.launch(exceptionHandler) {
            uiState = try {
                val topRated = async {getTopRatedMovies()}.await()
                val upcoming = async { getUpcomingMovies() }.await()
                MoviesHomeUiState.Success(MoviesHomeState(topRated, upcoming))
            } catch (e: Exception) {
                MoviesHomeUiState.Error
            }
        }

    }


    private suspend fun getTopRatedMovies(): Movies {
        val endUrl = "movie/top_rated?api_key=$API_KEY"
        return moviesRepository.getMovies(endUrl)
    }
    private suspend fun getUpcomingMovies(): Movies {
        val endUrl = "movie/upcoming?api_key=$API_KEY"
        return moviesRepository.getMovies(endUrl)
    }

    init {
        observeNetworkAndFetch()
        loadLiked()
    }
    private fun observeNetworkAndFetch() {
        viewModelScope.launch {
            connectivityRepository.connectivityState.collect { netState ->
                if (netState == ConnectivityState.Available) {
                    fetchData()
                } else {
                    uiState = MoviesHomeUiState.Error
                }
            }
        }
    }

    var likedMovies by mutableStateOf<List<LikedMovieEntity>>(emptyList())
    private fun loadLiked() {
        viewModelScope.launch {
            moviesRepository.getLikedMovies().collect { movies ->
                likedMovies = movies
            }
        }
    }

    fun isLiked(movieId: Int): Boolean {
        return likedMovies.any { it.id == movieId }
    }

}