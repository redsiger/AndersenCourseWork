package com.example.androidschool.data.database.episodes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidschool.domain.episode.model.EpisodeDetails

@Entity(tableName = "episode_details")
data class EpisodeDetailsRoom(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: Int,
    @ColumnInfo(name = "air_date")
    val airDate: String,
    @ColumnInfo(name = "characters")
    val characters: List<String>,
    @ColumnInfo(name = "episode")
    val episode: String,
    @ColumnInfo(name = "season")
    val season: String,
    @ColumnInfo(name = "series")
    val series: String,
    @ColumnInfo(name = "title")
    val title: String
) {
    fun toDomainModel(): EpisodeDetails {
        return EpisodeDetails(
            airDate = this.airDate,
            characters = this.characters,
            episode = this.episode,
            episodeId = this.episodeId,
            season = this.season,
            series = this.series,
            title = this.title
        )
    }
}