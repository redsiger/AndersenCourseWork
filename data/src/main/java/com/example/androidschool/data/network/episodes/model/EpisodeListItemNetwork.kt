package com.example.androidschool.data.network.episodes.model

import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeListItemNetwork(
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
) {
    fun toDomainModel(): EpisodeListItem {
        return EpisodeListItem(
            airDate = airDate,
            characters = characters,
            episode = episode,
            episodeId = episodeId,
            season = season,
            series = series,
            title = title
        )
    }

    fun toDomainDetailsModel(): EpisodeDetails {
        return EpisodeDetails(
            airDate = airDate,
            characters = characters,
            episode = episode,
            episodeId = episodeId,
            season = season,
            series = series,
            title = title
        )
    }
}

