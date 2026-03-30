package com.example.moviesapp.pages.movie_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.ConnectivityRepository
import com.example.moviesapp.data.ConnectivityState
import com.example.moviesapp.data.Movies
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.entities.toEntity
import com.example.moviesapp.data.entities.toEntityWatch

import com.example.moviesapp.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
): ViewModel() {
    var uiState: MovieDetailsUiState by mutableStateOf(MovieDetailsUiState.Loading)

    private fun fetchMovie(movieId: Int) {
        viewModelScope.launch {
            uiState = try {
                val movie = async { getMovieDetails(movieId) }.await()
                val similarMovies = async { getSimilarMovies(movieId) }.await()
        //                moviesRepository.getLikedMovies().collect { likedMovies ->
        //
        //                    val isLiked = likedMovies.any { it.id == movieId }
        //
        //                    uiState = MovieDetailsUiState.Success(
        //                        MovieDetailsState(movie, similarMovies)
        //                    )
        //                }

                moviesRepository.addToHistory(movie.toEntityWatch())
                MovieDetailsUiState.Success(MovieDetailsState(movie, similarMovies))
            } catch (e: Exception) {
                MovieDetailsUiState.Error
            }
        }
    }

//    fun toggleLike(movie: Movies.Movie) {
//        val currentState = uiState
//        if (currentState !is MovieDetailsUiState.Success) return
//        viewModelScope.launch {
//            val isCurrentlyLiked = currentState.movieDetailsState.isLiked
//            if (isCurrentlyLiked) {
//                moviesRepository.removeLike(movie.toEntity())
//            } else {
//                moviesRepository.likeMovie(movie.toEntity())
//            }
//
//            uiState = currentState.copy(
//                movieDetailsState = currentState.movieDetailsState.copy(isLiked = !isCurrentlyLiked)
//            )
//        }
//    }


    private suspend fun getMovieDetails(movieId: Int): Movies.Movie {
        val endUrl = "movie/${movieId}?api_key=$API_KEY"
        return moviesRepository.getMovieDetails(endUrl)
    }

    private suspend fun getSimilarMovies(movieId: Int): Movies {
        val endUrl = "movie/${movieId}/similar?api_key=$API_KEY"
        return moviesRepository.getMovies(endUrl)
    }



    fun observeNetworkAndFetch(movieId: Int) {
        viewModelScope.launch {
            connectivityRepository.connectivityState.collect { netState ->
                if (netState == ConnectivityState.Available) {
                    fetchMovie(movieId)
                } else {
                    uiState = MovieDetailsUiState.Error
                }
            }
        }
    }
}