package com.example.moviesapp.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.data.entities.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeMovie(movie: LikedMovieEntity)

    @Delete
    suspend fun removeLike(movie: LikedMovieEntity)

    @Query("SELECT * from liked_movies")
    fun getLikedMovies(): Flow<List<LikedMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM liked_movies WHERE id = :id)")
    suspend fun isMovieLiked(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(movie: WatchHistoryEntity)

    @Query("SELECT * FROM watch_history ORDER BY watched_at DESC")
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Query("DELETE FROM watch_history WHERE id IN (SELECT id FROM watch_history ORDER BY watched_at ASC LIMIT :count)")
    suspend fun deleteOldest(count: Int)

    @Query("SELECT COUNT(*) FROM watch_history")
    suspend fun getHistoryCount(): Int
}