package com.example.androidschool.domain.characters.repository

import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersListRepository {

    suspend fun getCharactersInEpisode(charactersName: List<String>): NetworkResponse<List<CharacterInEpisode>>

    suspend fun getCharactersPagingState(offset: Int, limit: Int): NetworkResponse<List<CharacterListItem>>

    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
}