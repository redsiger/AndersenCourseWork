package com.example.androidschool.domain.episode.interactors

import com.example.androidschool.domain.episode.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse

interface EpisodesListInteractor {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): NetworkResponse<List<EpisodeListItem>>

    class Base(private val repository: EpisodesListRepository): EpisodesListInteractor {

        override suspend fun getEpisodesPaging(offset: Int, limit: Int): NetworkResponse<List<EpisodeListItem>> {
            return repository.getEpisodesPaging(offset, limit)
        }
    }

}