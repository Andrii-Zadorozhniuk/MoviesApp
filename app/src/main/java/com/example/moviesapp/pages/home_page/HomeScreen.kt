package com.example.moviesapp.pages.home_page

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.moviesapp.data.Movies
import com.example.moviesapp.pages.search_page.NoResult
import com.example.moviesapp.widgets.LoadingScreen
import com.example.moviesapp.widgets.movie_item.MovieItem

@Composable
fun HomeScreen(
    onMovieClick: (id: Int) -> Unit,
    moviesViewModel: MoviesHomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {

    val state = moviesViewModel.uiState

    when(state) {
        is MoviesHomeUiState.Success -> HomeSection(onMovieClick, state.movies)
        is MoviesHomeUiState.Loading -> LoadingScreen()
        is MoviesHomeUiState.Error -> NoResult("Error appeared. Check your internet connection.")
    }
}


@Composable
fun HomeSection(
    onMovieClick: (id: Int) -> Unit,
    movies: MoviesHomeState,

    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val topRated = movies.topRatedMovies.results ?: emptyList()
        val upcoming = movies.upcomingMovies.results ?: emptyList()
        Spacer(modifier = Modifier.height(64.dp))
        LazyColumn {
            item {
                Text("Upcoming Movies", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                LazyRow {
                    items(upcoming.size) { index ->
                        MovieItem(
                            upcoming[index]!!, onMovieClick
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text("Top Rated Movies", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
            items(topRated.size) { index ->
                MovieItem(
                    topRated[index]!!, onMovieClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }

        }
    }
}