package com.example.androidschool.data.database.episodes

import androidx.room.*
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom

@Dao
interface EpisodesListDao {

    @Transaction
    suspend fun insertAndReturnEpisodesPaging(
        episodes: List<EpisodeListItemRoom>,
        offset: Int,
        limit: Int
    ): List<EpisodeListItemRoom> {
        insertEpisodes(episodes)
        return getEpisodesPaging(offset, limit, "Breaking Bad")
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeListItemRoom>)

    @Query(
        value = "SELECT * FROM episodes_list_items " +
                "WHERE episode_id > :offset " +
                "AND episode_id <= (:offset + :limit) " +
                "AND series = :series"
    )
    suspend fun getEpisodesPaging(
        offset: Int,
        limit: Int,
        series: String
    ): List<EpisodeListItemRoom>

    @Transaction
    suspend fun insertAndReturnAppearance(
        episodes: List<EpisodeListItemRoom>,
        appearanceList: List<Int>
    ): List<EpisodeListItemRoom> {
        insertEpisodes(episodes)
        return getAppearanceList(appearanceList)
    }

    @Transaction
    suspend fun getAppearanceList(appearanceList: List<Int>): List<EpisodeListItemRoom> {
        val list = mutableListOf<EpisodeListItemRoom>()
        appearanceList.forEach { episode_id ->
            list += getAppearance(episode_id)
        }
        return list.toList()
    }

    @Query("SELECT * FROM episodes_list_items WHERE episode_id = :episode_id")
    suspend fun getAppearance(episode_id: Int): List<EpisodeListItemRoom>

    @Transaction
    suspend fun insertAndReturnEpisodesBySeason(
        episodes: List<EpisodeListItemRoom>,
        season: String
    ): List<EpisodeListItemRoom> {
        insertEpisodes(episodes)
        return getEpisodesBySeason(season)
    }

    @Query("SELECT * FROM episodes_list_items WHERE season LIKE :season AND series LIKE \"Breaking Bad\"")
    suspend fun getEpisodesBySeason(season: String): List<EpisodeListItemRoom>

    @Query("SELECT * FROM episodes_list_items WHERE title LIKE '%' || :query || '%' OR characters LIKE '%' || :query || '%'")
    suspend fun searchEpisodesByNameOrAppearance(query: String): List<EpisodeListItemRoom>
}