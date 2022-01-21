package com.example.androidschool.data.network

import com.example.androidschool.data.network.model.CharacterNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("characters")
    suspend fun getCharactersPaginated(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 10
    ): Response<List<CharacterNetworkEntity>>

    @GET("characters/{id}")
    suspend fun getCharacter(
        @Path("id") characterId: Int
    ): Response<List<CharacterNetworkEntity>>

}