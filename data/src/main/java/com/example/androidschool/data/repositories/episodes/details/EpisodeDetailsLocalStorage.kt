package com.example.androidschool.data.repositories.episodes.details

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodeDetailsDao
import com.example.androidschool.domain.episode.model.EpisodeDetails

interface EpisodeDetailsLocalStorage {

    suspend fun insertAndReturn(episode: EpisodeDetails): EpisodeDetails

    suspend fun getEpisodeDetails(id: Int): EpisodeDetails?

    class Base(
        private val dao: EpisodeDetailsDao,
        private val mapper: DatabaseMapper
    ): EpisodeDetailsLocalStorage {

        override suspend fun getEpisodeDetails(id: Int): EpisodeDetails? {
            return try {
                dao.getEpisodeDetails(id).toDomainModel()
            } catch (e: Exception) { null }
        }

        override suspend fun insertAndReturn(episode: EpisodeDetails): EpisodeDetails {
            return dao.insertAndReturn(
                mapper.toRoomEntity(episode)
            ).toDomainModel()
        }

    }
}