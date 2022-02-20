package com.example.androidschool.domain.episode.interactor

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.Status

interface EpisodesListInteractor : BasePagingInteractor<EpisodeListItem> {

    suspend fun getCharacterAppearance(appearanceList: List<Int>): Status<List<EpisodeListItem>>

    suspend fun getEpisodesBySeason(season: String): Status<List<EpisodeListItem>>

    class Base(private val repository: EpisodesListRepository) : EpisodesListInteractor {

        override suspend fun getCharacterAppearance(appearanceList: List<Int>): Status<List<EpisodeListItem>> =
            repository.getCharacterAppearance(appearanceList)

        override suspend fun getEpisodesBySeason(season: String): Status<List<EpisodeListItem>> =
            repository.getEpisodesBySeason(season)

        override suspend fun getItemsPaging(
            offset: Int,
            limit: Int
        ): Status<List<EpisodeListItem>> =
            repository.getEpisodesPaging(offset, limit)
    }

}