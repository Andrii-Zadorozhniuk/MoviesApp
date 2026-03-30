package com.example.moviesapp.network

import com.example.moviesapp.data.Movies
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface MoviesApiService {
    @GET
    suspend fun getMovies(@Url endUrl: String): Movies

    @GET
    suspend fun searchMovies(@Url endUrl: String): Movies

    @GET
    suspend fun getMovieDetails(@Url endUrl: String): Movies.Movie
}





