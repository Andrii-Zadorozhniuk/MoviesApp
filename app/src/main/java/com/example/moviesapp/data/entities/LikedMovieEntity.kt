package com.example.moviesapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesapp.data.Movies

@Entity(tableName = "liked_movies")
data class LikedMovieEntity (
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
)

fun Movies.Movie.toEntity(): LikedMovieEntity {
    return LikedMovieEntity(
        id = this.id!!,
        title = this.title ?: "",
        backdropPath = this.backdropPath.toString(),
        voteAverage = this.voteAverage ?: 0.0
    )
}

fun LikedMovieEntity.toMovie(): Movies.Movie {
    return Movies.Movie(
        id = this.id,
        title = this.title,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage
    )
}