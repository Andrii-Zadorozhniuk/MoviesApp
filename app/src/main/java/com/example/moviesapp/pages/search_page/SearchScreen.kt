package com.example.moviesapp.pages.search_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.moviesapp.data.Movies
import com.example.moviesapp.widgets.LoadingScreen
import com.example.moviesapp.widgets.movie_item.MovieItem

@Composable
fun SearchScreen(
    onMovieClick: (id: Int) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {

    val state = searchViewModel.uiState
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        TextField(
            value = searchViewModel.searchQuery,
            onValueChange = {searchViewModel.onQueryChanged(it)},
            placeholder = {Text("Search movies...", color = Color.Gray)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFF2D2D2D),
                unfocusedContainerColor = Color(0xFF2D2D2D),
                cursorColor = Color.White,
                focusedTextColor = Color(0xFFDCDCDC)
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))
        when(state) {
            is SearchUiState.Success -> FoundSection(state.movies, onMovieClick)
            is SearchUiState.Searching -> LoadingScreen()
            is SearchUiState.Empty -> NoResult("Type at least 3 letters")
            is SearchUiState.NoResults -> NoResult("Nothing found.")
            is SearchUiState.NoInternet -> NoResult("No internet connection.")
        }
    }

}


@Composable
fun FoundSection(
    movies: Movies,
    onMovieClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier) {
    val moviesList = movies.results ?: emptyList()
    LazyColumn {
        items(moviesList.size) { index ->
            MovieItem(onClick = onMovieClick, movie =  moviesList[index]!!)
        }
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}


@Composable
fun NoResult(
    text: String,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}