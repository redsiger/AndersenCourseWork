package com.example.androidschool.data.database.episodes

import androidx.room.*
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom

@Dao
interface EpisodesListDao {

    @Transaction
    suspend fun insertAndReturn(
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
}