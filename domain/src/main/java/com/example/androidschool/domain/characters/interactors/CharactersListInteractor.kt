package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersListInteractor {

    suspend fun getCharactersPagingState(offset: Int, limit: Int): NetworkResponse<List<CharacterListItem>>
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>

    class Base(private val repository: CharactersListRepository): CharactersListInteractor {
        override suspend fun getCharactersPagingState(
            offset: Int,
            limit: Int
        ): NetworkResponse<List<CharacterListItem>>
            = repository.getCharactersPagingState(offset, limit)

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
            = repository.searchCharactersByNameOrNickName(query)
    }

}