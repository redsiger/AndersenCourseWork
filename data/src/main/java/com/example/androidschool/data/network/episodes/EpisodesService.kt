package com.example.androidschool.data.network.episodes

import com.example.androidschool.data.network.episodes.model.EpisodeListItemNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodesService {

    @GET("episodes")
    suspend fun getEpisodes(): Response<List<EpisodeListItemNetwork>>

    @GET("episodes/{id}")
    suspend fun getEpisode(
        @Path("id") episodeId: Int
    ): Response<List<EpisodeListItemNetwork>>

}