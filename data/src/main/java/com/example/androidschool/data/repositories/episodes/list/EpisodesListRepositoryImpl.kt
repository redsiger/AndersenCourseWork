package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.network.episodes.model.EpisodeListItemNetwork
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse
import retrofit2.HttpException
import java.lang.Exception

class EpisodesListRepositoryImpl(
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
                val remoteData = (response.body() as List<EpisodeListItemNetwork>)
                // imitation of paging because API service don't support paging
                val partialData = remoteData
                    .subList(
                    fromIndex = offset.coerceAtMost(remoteData.size),
                    toIndex = (offset + limit).coerceAtMost(remoteData.size)
                ).map(EpisodeListItemNetwork::toDomainModel)

                val data = localStorage.insertAndReturnEpisodesPaging(partialData, offset, limit)
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

    override suspend fun getCharacterAppearance(appearanceList: List<Int>): NetworkResponse<List<EpisodeListItem>> {
        return try {
            // imitation of paging because API service don't support paging
            val response = service.getEpisodes()
            if (response.isSuccessful) {
                val remoteData = (response.body() as List<EpisodeListItemNetwork>)
                // imitation of paging because API service don't support paging
                val partialData = remoteData.filter { episode ->
                    filterAppearance(episode, appearanceList)
                }.map(EpisodeListItemNetwork::toDomainModel)

                val data = localStorage.insertAndReturnAppearance(partialData, appearanceList)
                NetworkResponse.Success(data)
            }
            else {
                val localData = localStorage.getAppearanceList(appearanceList)
                val exception = response.errorBody() as HttpException
                NetworkResponse.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getAppearanceList(appearanceList)
            NetworkResponse.Error(localData, e)
        }
    }

    private fun filterAppearance(episode: EpisodeListItemNetwork, appearanceList: List<Int>): Boolean {
        appearanceList.forEach {
            if (episode.episodeId == it) return true
        }
        return false
    }
}


