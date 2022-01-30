package com.example.androidschool.domain.episode

import com.example.androidschool.domain.episode.model.EpisodeAttr
import com.example.androidschool.domain.episode.model.EpisodeEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {

    suspend fun getAllEpisodes(): Status<List<EpisodeEntity>>
    suspend fun getEpisode(episodeAttr: EpisodeAttr): Status<EpisodeEntity>
    fun searchEpisodeByTitle(query: String): Flow<List<EpisodeEntity>>
}