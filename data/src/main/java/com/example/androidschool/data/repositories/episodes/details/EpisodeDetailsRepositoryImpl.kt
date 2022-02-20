package com.example.androidschool.data.repositories.episodes.details

import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.network.episodes.model.EpisodeListItemNetwork
import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.repository.EpisodeDetailsRepository
import com.example.androidschool.util.Status
import retrofit2.HttpException
import java.lang.Exception

class EpisodeDetailsRepositoryImpl(
    private val service: EpisodesService,
    private val localStorage: EpisodeDetailsLocalStorage
): EpisodeDetailsRepository {

    override suspend fun getEpisodeDetails(id: Int): Status<EpisodeDetails?> {
        return try {
            val response = service.getEpisodeDetails(id)
            if (response.isSuccessful) {
                val episode = (response.body() as List<EpisodeListItemNetwork>).first().toDomainDetailsModel()
                val data = localStorage.insertAndReturn(episode)
                Status.Success(data)
            } else {
                val localEpisode = localStorage.getEpisodeDetails(id)
                Status.Error(localEpisode, response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            val localEpisode = localStorage.getEpisodeDetails(id)
            Status.Error(localEpisode, e)
        }
    }
}