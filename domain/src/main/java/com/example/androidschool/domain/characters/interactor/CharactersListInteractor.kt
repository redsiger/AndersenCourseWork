package com.example.androidschool.domain.characters.interactor

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersListInteractor: BasePagingInteractor<CharacterListItem> {

    suspend fun getCharactersInEpisode(idList: List<String>): NetworkResponse<List<CharacterInEpisode>>

    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>

    class Base(private val repository: CharactersListRepository): CharactersListInteractor, BasePagingInteractor<CharacterListItem> {

        override suspend fun getCharactersInEpisode(idList: List<String>): NetworkResponse<List<CharacterInEpisode>> =
            repository.getCharactersInEpisode(idList)

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>> =
            repository.searchCharactersByNameOrNickName(query)

        override suspend fun getItemsPaging(offset: Int, limit: Int): NetworkResponse<List<CharacterListItem>> =
            repository.getCharactersPagingState(offset, limit)
    }

}