package com.example.androidschool.domain.episode.model

import com.example.androidschool.domain.search.model.SearchItem
import com.example.androidschool.domain.search.model.TYPE_EPISODE

data class EpisodeListItem(
    val airDate: String,
    val characters: List<String>,
    val episode: String,
    val episodeId: Int,
    val season: String,
    val series: String,
    val title: String,
    override val type: String = TYPE_EPISODE
) : SearchItem