package com.example.androidschool.feature_characters.data.network

import com.example.androidschool.feature_characters.data.network.model.CharacterNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersService {

    @GET("characters")
    suspend fun getCharactersPaginated(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 10
    ): Response<List<CharacterNetworkEntity>>
}