package com.example.moviesapp.hiltModules

import android.content.Context
import android.net.ConnectivityManager
import com.example.moviesapp.data.ConnectivityRepository
import com.example.moviesapp.data.DefaultConnectivityRepository
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.MoviesRepositoryImpl
import com.example.moviesapp.data.daos.MoviesDao
import com.example.moviesapp.network.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModules {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    fun provideMoviesApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }

    @Provides
    fun provideMoviesRepository(moviesApiService: MoviesApiService, dao: MoviesDao): MoviesRepository {
        return MoviesRepositoryImpl(moviesApiService, dao)
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(ConnectivityManager::class.java)
    }

    @Provides
    fun provideConnectivityRepository(connectivityManager: ConnectivityManager): ConnectivityRepository {
        return DefaultConnectivityRepository(connectivityManager)
    }

}