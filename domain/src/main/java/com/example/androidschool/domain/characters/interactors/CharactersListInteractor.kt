package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersListInteractor: BasePagingInteractor<CharacterListItem> {

    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>

    class Base(private val repository: CharactersListRepository): CharactersListInteractor, BasePagingInteractor<CharacterListItem> {

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
            = repository.searchCharactersByNameOrNickName(query)

        override suspend fun getItemsPaging(
            offset: Int,
            limit: Int
        ): NetworkResponse<List<CharacterListItem>> = repository.getCharactersPagingState(offset, limit)
    }

}