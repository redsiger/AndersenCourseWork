package com.example.androidschool.domain.characters.interactor

import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.ListItem
import com.example.androidschool.util.Status

interface CharactersListInteractor: BasePagingInteractor<ListItem.CharacterListItem> {

    suspend fun getCharactersInEpisode(charactersList: List<String>): Status<List<CharacterInEpisode>>

    class Base(private val repository: CharactersListRepository): CharactersListInteractor, BasePagingInteractor<ListItem.CharacterListItem> {

        override suspend fun getCharactersInEpisode(charactersList: List<String>): Status<List<CharacterInEpisode>> =
            repository.getCharactersInEpisode(charactersList)

        override suspend fun getItemsPaging(offset: Int, limit: Int): Status<List<ListItem.CharacterListItem>> =
            repository.getCharactersPagingState(offset, limit)
    }

}