package com.example.androidschool.domain.episode.interactors

import com.example.androidschool.domain.episode.EpisodesRepository
import com.example.androidschool.domain.episode.model.EpisodeAttr
import com.example.androidschool.domain.episode.model.EpisodeEntity
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface EpisodesInteractor {

    suspend fun getAllEpisodes(): NetworkResponse<List<EpisodeEntity>>
    suspend fun getEpisode(episodeAttr: EpisodeAttr): NetworkResponse<EpisodeEntity>
    fun searchEpisodeByTitle(query: String): Flow<List<EpisodeEntity>>

    class Base(private val repository: EpisodesRepository): EpisodesInteractor {

        override suspend fun getAllEpisodes(): NetworkResponse<List<EpisodeEntity>> {
            return repository.getAllEpisodes()
        }

        override suspend fun getEpisode(episodeAttr: EpisodeAttr): NetworkResponse<EpisodeEntity> {
            return repository.getEpisode(episodeAttr)
        }

        override fun searchEpisodeByTitle(query: String): Flow<List<EpisodeEntity>> {
            return repository.searchEpisodeByTitle(query)
        }

    }

}