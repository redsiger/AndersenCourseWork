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
            "AND series = :series")
    suspend fun getEpisodesPaging(offset: Int, limit: Int, series: String): List<EpisodeListItemRoom>

    @Transaction
    suspend fun insertAndReturnAppearance(
        episodes: List<EpisodeListItemRoom>,
        appearanceList: List<Int>
    ): List<EpisodeListItemRoom> {
        insertEpisodes(episodes)
        val list = mutableListOf<EpisodeListItemRoom>()
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
}