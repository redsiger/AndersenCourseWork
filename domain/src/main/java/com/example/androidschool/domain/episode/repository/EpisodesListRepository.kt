package com.example.androidschool.domain.episode.repository

import com.example.androidschool.domain.search.model.ListItem
import com.example.androidschool.util.Status

interface EpisodesListRepository {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): Status<List<ListItem.EpisodeListItem>>

    suspend fun getCharacterAppearance(appearanceInList: List<Int>): Status<List<ListItem.EpisodeListItem>>

    suspend fun getEpisodesBySeason(season: String): Status<List<ListItem.EpisodeListItem>>

    suspend fun searchEpisodesByNameOrAppearance(query: String): Status<List<ListItem>>

}