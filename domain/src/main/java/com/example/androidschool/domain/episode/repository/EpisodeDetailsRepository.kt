package com.example.androidschool.domain.episode.repository

import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.util.Status

interface EpisodeDetailsRepository {

    suspend fun getEpisodeDetails(id: Int): Status<EpisodeDetails?>
}