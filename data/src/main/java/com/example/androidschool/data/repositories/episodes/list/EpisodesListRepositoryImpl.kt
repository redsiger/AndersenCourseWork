package com.example.androidschool.data.repositories.episodes.list

import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.network.episodes.model.EpisodeListItemNetwork
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.Status
import retrofit2.HttpException
import java.lang.Exception

@Suppress("CAST_NEVER_SUCCEEDS")
class EpisodesListRepositoryImpl(
    private val service: EpisodesService,
    private val localStorage: EpisodesListLocalStorage
): EpisodesListRepository {

    override suspend fun getEpisodesPaging(
        offset: Int,
        limit: Int
    ): Status<List<EpisodeListItem>> {
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
                Status.Success(data)
            }
            else {
                val localData = localStorage.getEpisodesPaging(offset, limit)
                val exception = response.errorBody() as HttpException
                Status.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getEpisodesPaging(offset, limit)
            Status.Error(localData, e)
        }
    }

    override suspend fun getCharacterAppearance(appearanceInList: List<Int>): Status<List<EpisodeListItem>> {
        return try {
            val response = service.getEpisodes()
            if (response.isSuccessful) {
                val remoteData = (response.body() as List<EpisodeListItemNetwork>)

                val appearanceList = remoteData.filter { episode ->
                    filterAppearance(episode, appearanceInList)
                }.map(EpisodeListItemNetwork::toDomainModel)

                val data = localStorage.insertAndReturnAppearance(appearanceList, appearanceInList)
                Status.Success(data)
            }
            else {
                val localData = localStorage.getAppearanceList(appearanceInList)
                val exception = response.errorBody() as HttpException
                Status.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getAppearanceList(appearanceInList)
            Status.Error(localData, e)
        }
    }

    override suspend fun getEpisodesBySeason(season: String): Status<List<EpisodeListItem>> {
        return try {
            val response = service.getEpisodes()
            if (response.isSuccessful) {
                val remoteData = (response.body() as List<EpisodeListItemNetwork>)

                val appearanceList = remoteData.filter { episode ->
                    episode.season == season
                }.map(EpisodeListItemNetwork::toDomainModel)

                val data = localStorage.insertAndReturnEpisodesBySeason(appearanceList, season)
                Status.Success(data)
            }
            else {
                val localData = localStorage.getEpisodesBySeason(season)
                val exception = response.errorBody() as HttpException
                Status.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getEpisodesBySeason(season)
            Status.Error(localData, e)
        }
    }

    private fun filterAppearance(episode: EpisodeListItemNetwork, appearanceList: List<Int>): Boolean {
        appearanceList.forEach {
            if (episode.episodeId == it) return true
        }
        return false
    }
}


