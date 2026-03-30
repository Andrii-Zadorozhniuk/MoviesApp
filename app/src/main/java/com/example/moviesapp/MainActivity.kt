package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.data.Movies
import com.example.moviesapp.destinations.Screen

import com.example.moviesapp.pages.home_page.HomeScreen
import com.example.moviesapp.pages.home_page.MoviesHomeViewModel
import com.example.moviesapp.pages.movie_details.MovieDetailsScreen
import com.example.moviesapp.pages.movie_details.MovieDetailsViewModel
import com.example.moviesapp.pages.profile_page.ProfileScreen
import com.example.moviesapp.pages.search_page.SearchScreen
import com.example.moviesapp.pages.search_page.SearchViewModel
import com.example.moviesapp.ui.theme.BackgroundScreen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.ui.theme.Purple80
import com.example.moviesapp.widgets.FloatingNavBar
import com.example.moviesapp.widgets.NavBarItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                MoviesApp()
            }
        }
    }
}






@Composable
fun MoviesApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =  navBackStackEntry?.destination?.route

    val selectedTab = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Search.route -> 1
        Screen.Profile.route -> 2
        else -> 0
    }
    val navItems = listOf(
        NavBarItem(Icons.Default.Home, "Home"),
        NavBarItem(Icons.Default.Search, "Search"),
        NavBarItem(Icons.Default.Person, "Profile")
    )


    fun navigateToDetails(id: Int) {
        navController.navigate(Screen.Details.createRoute(id))
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundScreen)) {

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onMovieClick = {id -> navigateToDetails(id)})
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onMovieClick = {id -> navigateToDetails(id)})
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onMovieClick = {id ->
                    navController.navigate(
                        Screen.Details.createRoute(id)
                    )
                })
            }
            composable(Screen.Details.route) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
                if (movieId != null) {
                    MovieDetailsScreen(
                        movieId, movieDetailsViewModel,{id -> navigateToDetails(id)}, {navController.popBackStack()})
                }
            }
        }


        FloatingNavBar(
            opacity = 0.95f,
            widthFactor = 0.75f,
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedIndex = selectedTab,
            items = navItems,
            primaryColor = Purple80,
            backgroundColor = BackgroundScreen,
            onItemSelected = {
                val route = when(it) {
                    0 -> Screen.Home.route
                    1 -> Screen.Search.route
                    2 -> Screen.Profile.route
                    else -> Screen.Home.route
                }

                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }


}