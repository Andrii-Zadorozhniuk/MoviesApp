package com.example.moviesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesapp.data.daos.MoviesDao
import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.data.entities.WatchHistoryEntity


@Database(entities = [LikedMovieEntity::class, WatchHistoryEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        const val DATABASE_NAME = "movies_database"

        @Volatile
        private var Instance: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, MoviesDatabase::class.java, DATABASE_NAME
                ).build().also {
                    Instance = it
                }
            }
        }
    }
}