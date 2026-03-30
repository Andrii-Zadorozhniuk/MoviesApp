package com.example.moviesapp.widgets.movie_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.Movies
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.data.entities.toEntity
import com.example.moviesapp.pages.movie_details.MovieDetailsState
import com.example.moviesapp.pages.movie_details.MovieDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {


    private val _likedMovies = MutableStateFlow<List<LikedMovieEntity>>(emptyList())
    val likedMovies = _likedMovies.asStateFlow()

    init {
        loadLikes()
    }

    private fun loadLikes() {
        viewModelScope.launch {
            moviesRepository.getLikedMovies().collect { movies ->
                _likedMovies.value = movies
            }
        }
    }

    // Проверка лайка через Flow, возвращает StateFlow<Boolean>
    fun isLikedFlow(movieId: Int): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        viewModelScope.launch {
            moviesRepository.getLikedMovies().collect { likedMovies ->
                result.value = likedMovies.any { it.id == movieId }
            }
        }
        return result
    }



    // переключение лайка
    fun toggleLike(movie: Movies.Movie) {
        viewModelScope.launch {
            val entity = movie.toEntity()
            val liked = _likedMovies.value.any { it.id == movie.id }
            if (liked) {
                moviesRepository.removeLike(entity)
            } else {
                moviesRepository.likeMovie(entity)
            }
        }
    }
}