package com.example.moviesapp.pages.profile_page


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
): ViewModel() {
    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                kotlinx.coroutines.flow.combine(
                    moviesRepository.getLikedMovies(),
                    moviesRepository.getWatchHistory()
                ) { liked, history ->

                    ProfileState(
                        likedMovies = liked,
                        watchHistory = history
                    )

                }.collect { profileState ->

                    _profileUiState.value = ProfileUiState.Success(profileState)

                }

            } catch (e: Exception) {
                _profileUiState.value = ProfileUiState.Error
            }
        }
    }
}