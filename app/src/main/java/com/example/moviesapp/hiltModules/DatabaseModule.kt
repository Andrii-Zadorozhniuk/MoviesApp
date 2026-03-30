package com.example.moviesapp.hiltModules

import android.content.Context
import com.example.moviesapp.data.MoviesDatabase
import com.example.moviesapp.data.daos.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providesMoviesDao(@ApplicationContext context: Context): MoviesDao {
        return MoviesDatabase.getDatabase(context).moviesDao()
    }

}