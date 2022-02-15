package com.example.androidschool.andersencoursework.ui.edpisode.models

import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable

data class EpisodeListItemUI(
    val airDate: String,
    val characters: List<String>,
    val episode: String,
    val episodeId: Int,
    val season: String,
    val series: String,
    val title: String,
    override val id: Int = episodeId
): DiffComparable