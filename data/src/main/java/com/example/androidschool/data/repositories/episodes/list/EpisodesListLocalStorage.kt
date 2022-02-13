package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom
import com.example.androidschool.domain.episode.model.EpisodeListItem

interface EpisodesListLocalStorage {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): List<EpisodeListItem>

    suspend fun insertAndReturn(
        episodes: List<EpisodeListItem>,
        offset: Int,
        limit: Int
    ): List<EpisodeListItem>

    class Base(
        private val dao: EpisodesListDao,
        private val mapper: DatabaseMapper
    ): EpisodesListLocalStorage {

        override suspend fun getEpisodesPaging(offset: Int, limit: Int): List<EpisodeListItem> {
            return dao.getEpisodesPaging(offset, limit).map(EpisodeListItemRoom::toDomainModel)
        }

        override suspend fun insertAndReturn(
            episodes: List<EpisodeListItem>,
            offset: Int,
            limit: Int
        ): List<EpisodeListItem> {
            return dao.insertAndReturn(
                episodes.map(mapper::toRoomEntity),
                offset,
                limit
            ).map(EpisodeListItemRoom::toDomainModel)
        }
    }
}