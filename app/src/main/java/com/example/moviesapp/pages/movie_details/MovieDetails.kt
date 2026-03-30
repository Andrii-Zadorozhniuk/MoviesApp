package com.example.moviesapp.pages.movie_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.R
import com.example.moviesapp.pages.search_page.NoResult
import com.example.moviesapp.widgets.LoadingScreen
import com.example.moviesapp.widgets.movie_item.LikesViewModel
import com.example.moviesapp.widgets.movie_item.MovieItem

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    movieDetailsViewModel: MovieDetailsViewModel,
    onMovieClick: (id: Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier) {
    val likesViewModel: LikesViewModel = hiltViewModel()
    movieDetailsViewModel.observeNetworkAndFetch(movieId)
    val state = movieDetailsViewModel.uiState
    when(state) {
        is MovieDetailsUiState.Success -> MovieDetailsSection(
            state.movieDetailsState,onMovieClick,
            navigateBack, likesViewModel)
        is MovieDetailsUiState.Loading -> LoadingScreen()
        is MovieDetailsUiState.Error -> NoResult("Error appeared. Check your internet connection.")
    }

}


@Composable
fun MovieDetailsSection(
    details: MovieDetailsState,
    onMovieClick: (id: Int) -> Unit,
    navigateBack: () -> Unit,
    likesViewModel: LikesViewModel,
    modifier: Modifier = Modifier) {
    val movie = details.movie
    val isLiked by likesViewModel.isLikedFlow(movie.id!!).collectAsState()
    val similarMovies = details.similarMovies.results ?: emptyList()

    fun toggleLike() {
        likesViewModel.toggleLike(movie)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/original${movie.backdropPath}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(R.drawable.ic_launcher_background),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            IconButton(
                onClick = navigateBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 45.dp, start = 25.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {toggleLike()},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 45.dp, end = 25.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(movie.title.toString(), maxLines = 2, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
                overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)

        }
        LazyColumn(modifier= Modifier.padding(horizontal = 12.dp, vertical = 20.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val rating = ((movie.voteAverage ?: 0f).toDouble() / 2).coerceIn(0.0, 5.0)
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }

            Spacer(modifier = Modifier.height(15.dp))

            Text(movie.overview.toString(), color = Color.LightGray)
            Spacer(modifier = Modifier.height(25.dp))
            Text("Similar Movies", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            LazyRow {
                items(similarMovies.size) { index ->
                    MovieItem(onClick = onMovieClick, movie =  similarMovies[index]!!)
                }
            }
            Spacer(modifier = Modifier.height(64.dp)) }
        }

    }
}