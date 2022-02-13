package com.example.androidschool.data.database.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
        return getEpisodesPaging(offset, limit)
    }

    @Insert
    suspend fun insertEpisodes(episodes: List<EpisodeListItemRoom>)

    @Query("SELECT * FROM episodes_list_items WHERE episode_id > :offset AND episode_id <= (:offset + :limit) ")
    suspend fun getEpisodesPaging(offset: Int, limit: Int): List<EpisodeListItemRoom>
}