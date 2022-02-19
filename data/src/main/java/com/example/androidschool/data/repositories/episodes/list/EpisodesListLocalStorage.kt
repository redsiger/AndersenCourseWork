package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom
import com.example.androidschool.domain.episode.model.EpisodeListItem

interface EpisodesListLocalStorage {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): List<EpisodeListItem>

    suspend fun insertAndReturnEpisodesPaging(
        episodes: List<EpisodeListItem>,
        offset: Int,
        limit: Int
    ): List<EpisodeListItem>

    suspend fun getAppearanceList(appearanceList: List<Int>): List<EpisodeListItem>

    suspend fun insertAndReturnAppearance(
        episodes: List<EpisodeListItem>,
        appearance: List<Int>
    ): List<EpisodeListItem>

    suspend fun getEpisodesBySeason(season: String): List<EpisodeListItem>

    suspend fun insertAndReturnEpisodesBySeason(
        episodes: List<EpisodeListItem>,
        season: String
    ): List<EpisodeListItem>

    class Base(
        private val dao: EpisodesListDao,
        private val mapper: DatabaseMapper
    ): EpisodesListLocalStorage {

        override suspend fun getEpisodesPaging(offset: Int, limit: Int): List<EpisodeListItem> =
            dao.getEpisodesPaging(offset, limit, "Breaking Bad").map(EpisodeListItemRoom::toDomainModel)


        override suspend fun insertAndReturnEpisodesPaging(
            episodes: List<EpisodeListItem>,
            offset: Int,
            limit: Int
        ): List<EpisodeListItem> {
            return dao.insertAndReturnEpisodesPaging(
                episodes.map(mapper::toRoomEntity),
                offset,
                limit
            ).map(EpisodeListItemRoom::toDomainModel)
        }

        override suspend fun getAppearanceList(appearanceList: List<Int>): List<EpisodeListItem> =
            dao.getAppearanceList(appearanceList).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun insertAndReturnAppearance(
            episodes: List<EpisodeListItem>,
            appearance: List<Int>
        ): List<EpisodeListItem> =
            dao.insertAndReturnAppearance(
                episodes.map(mapper::toRoomEntity),
                appearance
            ).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun getEpisodesBySeason(season: String): List<EpisodeListItem> =
            dao.getEpisodesBySeason(season).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun insertAndReturnEpisodesBySeason(
            episodes: List<EpisodeListItem>,
            season: String
        ): List<EpisodeListItem> =
            dao.insertAndReturnEpisodesBySeason(
                episodes.map(mapper::toRoomEntity),
                season
            ).map(EpisodeListItemRoom::toDomainModel)


    }
}