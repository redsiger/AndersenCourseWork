package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom
import com.example.androidschool.domain.search.model.ListItem

interface EpisodesListLocalStorage {

    suspend fun getEpisodesPaging(offset: Int, limit: Int): List<ListItem.EpisodeListItem>

    suspend fun insertAndReturnEpisodesPaging(
        episodes: List<ListItem.EpisodeListItem>,
        offset: Int,
        limit: Int
    ): List<ListItem.EpisodeListItem>

    suspend fun insertEpisodes(episodes: List<ListItem.EpisodeListItem>)

    suspend fun getAppearanceList(appearanceList: List<Int>): List<ListItem.EpisodeListItem>

    suspend fun insertAndReturnAppearance(
        episodes: List<ListItem.EpisodeListItem>,
        appearance: List<Int>
    ): List<ListItem.EpisodeListItem>

    suspend fun getEpisodesBySeason(season: String): List<ListItem.EpisodeListItem>

    suspend fun insertAndReturnEpisodesBySeason(
        episodes: List<ListItem.EpisodeListItem>,
        season: String
    ): List<ListItem.EpisodeListItem>

    suspend fun searchEpisodesByNameOrAppearance(query: String): List<ListItem.EpisodeListItem>

    class Base(
        private val dao: EpisodesListDao,
        private val mapper: DatabaseMapper
    ) : EpisodesListLocalStorage {

        override suspend fun getEpisodesPaging(offset: Int, limit: Int): List<ListItem.EpisodeListItem> =
            dao.getEpisodesPaging(offset, limit, "Breaking Bad")
                .map(EpisodeListItemRoom::toDomainModel)


        override suspend fun insertAndReturnEpisodesPaging(
            episodes: List<ListItem.EpisodeListItem>,
            offset: Int,
            limit: Int
        ): List<ListItem.EpisodeListItem> {
            return dao.insertAndReturnEpisodesPaging(
                episodes.map(mapper::toRoomEntity),
                offset,
                limit
            ).map(EpisodeListItemRoom::toDomainModel)
        }

        override suspend fun insertEpisodes(episodes: List<ListItem.EpisodeListItem>) =
            dao.insertEpisodes(episodes.map(mapper::toRoomEntity))

        override suspend fun getAppearanceList(appearanceList: List<Int>): List<ListItem.EpisodeListItem> =
            dao.getAppearanceList(appearanceList).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun insertAndReturnAppearance(
            episodes: List<ListItem.EpisodeListItem>,
            appearance: List<Int>
        ): List<ListItem.EpisodeListItem> =
            dao.insertAndReturnAppearance(
                episodes.map(mapper::toRoomEntity),
                appearance
            ).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun getEpisodesBySeason(season: String): List<ListItem.EpisodeListItem> =
            dao.getEpisodesBySeason(season).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun insertAndReturnEpisodesBySeason(
            episodes: List<ListItem.EpisodeListItem>,
            season: String
        ): List<ListItem.EpisodeListItem> =
            dao.insertAndReturnEpisodesBySeason(
                episodes.map(mapper::toRoomEntity),
                season
            ).map(EpisodeListItemRoom::toDomainModel)

        override suspend fun searchEpisodesByNameOrAppearance(query: String): List<ListItem.EpisodeListItem> =
            dao.searchEpisodesByNameOrAppearance(query).map(EpisodeListItemRoom::toDomainModel)


    }
}