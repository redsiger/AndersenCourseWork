package com.example.androidschool.domain.episode.repository

import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.Status

interface EpisodesListRepository {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): Status<List<EpisodeListItem>>

    suspend fun getCharacterAppearance(appearanceInList: List<Int>): Status<List<EpisodeListItem>>

    suspend fun getEpisodesBySeason(season: String): Status<List<EpisodeListItem>>

}