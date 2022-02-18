package com.example.androidschool.domain.episode.interactors

import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.repository.EpisodeDetailsRepository
import com.example.androidschool.util.NetworkResponse

interface EpisodeDetailsInteractor {

    suspend fun getEpisodeDetails(id: Int): NetworkResponse<EpisodeDetails?>

    class Base(private val repository: EpisodeDetailsRepository): EpisodeDetailsInteractor {

        override suspend fun getEpisodeDetails(id: Int): NetworkResponse<EpisodeDetails?> =
            repository.getEpisodeDetails(id)
    }
}