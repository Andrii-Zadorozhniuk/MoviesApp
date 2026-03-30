package com.example.moviesapp.data

import com.example.moviesapp.data.daos.MoviesDao
import com.example.moviesapp.data.entities.LikedMovieEntity
import com.example.moviesapp.data.entities.WatchHistoryEntity
import com.example.moviesapp.network.MoviesApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getMovies(endUrl: String): Movies
    suspend fun searchMovies(endUrl: String): Movies
    suspend fun getMovieDetails(endUrl: String): Movies.Movie

    //db
    suspend fun likeMovie(movie: LikedMovieEntity)
    suspend fun removeLike(movie: LikedMovieEntity)
    fun getLikedMovies(): Flow<List<LikedMovieEntity>>
    suspend fun isMovieLiked(id: Int): Boolean
    suspend fun addToHistory(movie: WatchHistoryEntity)
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

}

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApiService: MoviesApiService,
    private val dao: MoviesDao
): MoviesRepository {
    override suspend fun getMovies(endUrl: String): Movies {
        return moviesApiService.getMovies(endUrl)
    }


    override suspend fun searchMovies(endUrl: String): Movies {
        return moviesApiService.searchMovies(endUrl)
    }

    override suspend fun getMovieDetails(endUrl: String): Movies.Movie {
        return moviesApiService.getMovieDetails(endUrl)
    }



    //db
    override suspend fun likeMovie(movie: LikedMovieEntity) {
        dao.likeMovie(LikedMovieEntity(movie.id, movie.title, movie.backdropPath, movie.voteAverage))
    }

    override suspend fun removeLike(movie: LikedMovieEntity) {
        dao.removeLike(LikedMovieEntity(movie.id, movie.title, movie.backdropPath, movie.voteAverage))
    }

    override fun getLikedMovies(): Flow<List<LikedMovieEntity>> = dao.getLikedMovies()

    override suspend fun isMovieLiked(id: Int): Boolean = dao.isMovieLiked(id)

    override suspend fun addToHistory(movie: WatchHistoryEntity) {
        val currentCount = dao.getHistoryCount()
        if (currentCount >= 50) {
            val overflow = currentCount - 49
            dao.deleteOldest(overflow)
        }
        dao.addToHistory(movie)
    }

    override fun getWatchHistory(): Flow<List<WatchHistoryEntity>> = dao.getWatchHistory()



}



