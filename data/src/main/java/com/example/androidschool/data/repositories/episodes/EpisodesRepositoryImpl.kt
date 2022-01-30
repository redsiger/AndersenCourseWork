package com.example.androidschool.data.repositories.episodes

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesDao
import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.domain.episode.EpisodesRepository
import com.example.androidschool.domain.episode.model.EpisodeAttr
import com.example.androidschool.domain.episode.model.EpisodeEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

class EpisodesRepositoryImpl(
    private val service: EpisodesService,
    private val dao: EpisodesDao,
    private val mapper: DatabaseMapper
): EpisodesRepository {

    override suspend fun getAllEpisodes(): Status<List<EpisodeEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisode(episodeAttr: EpisodeAttr): Status<EpisodeEntity> {
        TODO("Not yet implemented")
    }

    override fun searchEpisodeByTitle(query: String): Flow<List<EpisodeEntity>> {
        TODO("Not yet implemented")
    }
}