package com.example.androidschool.domain.characters.interactor

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersListInteractor: BasePagingInteractor<CharacterListItem> {

    suspend fun getCharactersInEpisode(charactersList: List<String>): Status<List<CharacterInEpisode>>

    class Base(private val repository: CharactersListRepository): CharactersListInteractor, BasePagingInteractor<CharacterListItem> {

        override suspend fun getCharactersInEpisode(charactersList: List<String>): Status<List<CharacterInEpisode>> =
            repository.getCharactersInEpisode(charactersList)

        override suspend fun getItemsPaging(offset: Int, limit: Int): Status<List<CharacterListItem>> =
            repository.getCharactersPagingState(offset, limit)
    }

}