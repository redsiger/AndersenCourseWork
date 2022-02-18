package com.example.androidschool.data.database.episodes

import androidx.room.*
import com.example.androidschool.data.database.episodes.model.EpisodeDetailsRoom

@Dao
interface EpisodeDetailsDao {

    @Query("SELECT * FROM episode_details WHERE episode_id = :id")
    suspend fun getEpisodeDetails(id: Int): EpisodeDetailsRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodeDetails(episodeDetails: EpisodeDetailsRoom)

    @Transaction
    suspend fun insertAndReturn(episodeDetails: EpisodeDetailsRoom): EpisodeDetailsRoom {
        insertEpisodeDetails(episodeDetails)
        return getEpisodeDetails(episodeDetails.episodeId)
    }
}