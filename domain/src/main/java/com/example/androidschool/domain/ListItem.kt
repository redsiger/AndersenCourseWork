package com.example.androidschool.domain

import com.example.androidschool.domain.search.ListItemType


sealed interface ListItem {

    val listItemType: ListItemType

    data class CharacterListItem(
        val appearance: List<Int> = emptyList(),
        val betterCallSaulAppearance: List<Int> = emptyList(),
        val birthday: String = "",
        val category: String = "",
        val charId: Int = -1,
        val img: String = "",
        val name: String = "",
        val nickname: String = "",
        val occupation: List<String> = emptyList(),
        val portrayed: String = "",
        val status: String = "",
    ) : ListItem {
        override val listItemType: ListItemType
            get() = ListItemType.CHARACTER
    }

    data class EpisodeListItem(
        val airDate: String,
        val characters: List<String>,
        val episode: String,
        val episodeId: Int,
        val season: String,
        val series: String,
        val title: String
    ) : ListItem {
        override val listItemType: ListItemType
            get() = ListItemType.EPISODE
    }
}