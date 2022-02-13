package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.network.episodes.model.EpisodeListItemNetwork
import com.example.androidschool.domain.episode.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse
import retrofit2.HttpException
import java.lang.Exception

class EpisodesRepositoryImpl(
    private val service: EpisodesService,
    private val localStorage: EpisodesListLocalStorage
): EpisodesListRepository {

    override suspend fun getEpisodesPaging(
        offset: Int,
        limit: Int
    ): NetworkResponse<List<EpisodeListItem>> {
        return try {
            // imitation of paging because API service don't support paging
            val response = service.getEpisodes()
            if (response.isSuccessful) {
                val remoteData = response.body() as List<EpisodeListItemNetwork>
                // imitation of paging because API service don't support paging
                val partialData = remoteData.subList(
                    fromIndex = offset, toIndex =  (offset + limit)
                ).map(EpisodeListItemNetwork::toDomainModel)
                val data = localStorage.insertAndReturn(partialData, offset, limit)
                NetworkResponse.Success(data)
            }
            else {
                val localData = localStorage.getEpisodesPaging(offset, limit)
                val exception = response.errorBody() as HttpException
                NetworkResponse.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getEpisodesPaging(offset, limit)
            NetworkResponse.Error(localData, e)
        }
    }
}