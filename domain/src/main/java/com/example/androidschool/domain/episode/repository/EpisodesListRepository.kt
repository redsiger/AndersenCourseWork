package com.example.androidschool.domain.episode.repository

import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse

interface EpisodesListRepository {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): NetworkResponse<List<EpisodeListItem>>

    suspend fun getCharacterAppearance(appearanceInList: List<Int>): NetworkResponse<List<EpisodeListItem>>

    suspend fun getEpisodesBySeason(season: String): NetworkResponse<List<EpisodeListItem>>

}