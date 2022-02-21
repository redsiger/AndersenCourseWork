package com.example.androidschool.andersencoursework.ui.core.recycler

sealed interface ListItemUI: DiffComparable {

    data class CharacterListItemUI(
        val charId: Int,
        val appearance: List<Int>,
        val betterCallSaulAppearance: List<Int>,
        val birthday: String,
        val category: String,
        val img: String,
        val name: String,
        val nickname: String,
        val occupation: List<String>,
        val portrayed: String,
        val status: String,
        override val id: Int = charId
    ): ListItemUI

    data class EpisodeListItemUI(
        val airDate: String,
        val characters: List<String>,
        val episode: String,
        val episodeId: Int,
        val season: String,
        val series: String,
        val title: String,
        override val id: Int = episodeId
    ): ListItemUI
}