package com.example.moviesapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesapp.data.Movies

@Entity(tableName = "watch_history")
data class WatchHistoryEntity (
    @PrimaryKey val id: Int ,
    val title: String,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "vote_average") val voteAverage: String,
    @ColumnInfo(name = "watched_at") val watchedAt: Long,
)

fun Movies.Movie.toEntityWatch(): WatchHistoryEntity {
    return WatchHistoryEntity(
        id = this.id!!,
        title = this.title ?: "",
        backdropPath = this.backdropPath.toString(),
        voteAverage = this.voteAverage.toString(),
        watchedAt = 0
    )
}

fun WatchHistoryEntity.toMovie(): Movies.Movie {
    return Movies.Movie(
        id = this.id,
        title = this.title,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage.toDouble()
    )
}