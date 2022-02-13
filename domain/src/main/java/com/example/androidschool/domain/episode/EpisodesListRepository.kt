package com.example.androidschool.domain.episode

import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse

interface EpisodesListRepository {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): NetworkResponse<List<EpisodeListItem>>
}