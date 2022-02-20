package com.example.androidschool.domain.characters.repository

import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersListRepository {

    suspend fun getCharactersInEpisode(charactersName: List<String>): Status<List<CharacterInEpisode>>

    suspend fun getCharactersPagingState(offset: Int, limit: Int): Status<List<CharacterListItem>>

    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
}