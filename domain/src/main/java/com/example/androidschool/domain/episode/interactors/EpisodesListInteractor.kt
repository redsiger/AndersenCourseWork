package com.example.androidschool.domain.episode.interactors

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse

interface EpisodesListInteractor: BasePagingInteractor<EpisodeListItem> {

    suspend fun getCharacterAppearance(appearanceList: List<Int>): NetworkResponse<List<EpisodeListItem>>

    class Base(private val repository: EpisodesListRepository): EpisodesListInteractor {

        override suspend fun getCharacterAppearance(appearanceList: List<Int>): NetworkResponse<List<EpisodeListItem>> =
            repository.getCharacterAppearance(appearanceList)

        override suspend fun getItemsPaging(
            offset: Int,
            limit: Int
        ): NetworkResponse<List<EpisodeListItem>> {
            return repository.getEpisodesPaging(offset, limit)
        }
    }

}