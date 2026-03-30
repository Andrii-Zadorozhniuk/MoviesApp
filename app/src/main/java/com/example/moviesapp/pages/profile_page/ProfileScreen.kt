package com.example.moviesapp.pages.profile_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moviesapp.data.entities.toMovie
import com.example.moviesapp.pages.search_page.NoResult
import com.example.moviesapp.widgets.LoadingScreen
import com.example.moviesapp.widgets.movie_item.MovieItem

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier) {

    val state by viewModel.profileUiState.collectAsState()

    when (state) {
        ProfileUiState.Loading -> LoadingScreen()
        ProfileUiState.Error -> NoResult("Error loading profile")
        is ProfileUiState.Success -> {
            val profile = (state as ProfileUiState.Success).profile
            ProfileSection(profile, onMovieClick)
        }
    }

}


@Composable
fun ProfileSection(
    profile: ProfileState,
    onMovieClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier) {
    val liked = profile.likedMovies
    val watch = profile.watchHistory
    Column( modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Spacer(modifier = Modifier.height(64.dp))
        LazyColumn {
            item {
                if(liked.isNotEmpty()) Text("Liked Movies", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                LazyRow {
                    items(liked.size) { index ->
                        MovieItem(
                            liked[index].toMovie(), onMovieClick
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                if(watch.isNotEmpty())Text("Watch History", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
            items(watch.size) { index ->
                MovieItem(
                    watch[index].toMovie(), onMovieClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }

        }
    }

}