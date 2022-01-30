package com.example.androidschool.data.network.episodes.model

import com.example.androidschool.domain.episode.model.EpisodeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeNetworkEntity(
    @Json(name = "air_date")
    val airDate: String,
    @Json(name = "characters")
    val characters: List<String>,
    @Json(name = "episode")
    val episode: String,
    @Json(name = "episode_id")
    val episodeId: Int,
    @Json(name = "season")
    val season: String,
    @Json(name = "series")
    val series: String,
    @Json(name = "title")
    val title: String
)

fun EpisodeNetworkEntity.toDomainModel(): EpisodeEntity {
    return EpisodeEntity(
        airDate = this.airDate,
        characters = this.characters,
        episode = this.episode,
        episodeId = this.episodeId,
        season = this.season,
        series = this.series,
        title = this.title
    )
}