package com.example.androidschool.domain.episode.model

data class EpisodeListItem(
    val airDate: String,
    val characters: List<String>,
    val episode: String,
    val episodeId: Int,
    val season: String,
    val series: String,
    val title: String
)