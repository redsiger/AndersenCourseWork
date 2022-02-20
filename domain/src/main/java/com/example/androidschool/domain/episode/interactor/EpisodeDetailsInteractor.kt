package com.example.androidschool.domain.episode.interactor

import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.repository.EpisodeDetailsRepository
import com.example.androidschool.util.Status

interface EpisodeDetailsInteractor {

    suspend fun getEpisodeDetails(id: Int): Status<EpisodeDetails?>

    class Base(private val repository: EpisodeDetailsRepository): EpisodeDetailsInteractor {

        override suspend fun getEpisodeDetails(id: Int): Status<EpisodeDetails?> =
            repository.getEpisodeDetails(id)
    }
}