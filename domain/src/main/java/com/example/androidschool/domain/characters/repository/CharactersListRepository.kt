package com.example.androidschool.domain.characters.repository

import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.ListItem
import com.example.androidschool.util.Status

interface CharactersListRepository {

    suspend fun getCharactersInEpisode(charactersName: List<String>): Status<List<CharacterInEpisode>>

    suspend fun getCharactersPagingState(offset: Int, limit: Int): Status<List<ListItem.CharacterListItem>>

    suspend fun searchCharactersByNameOrNickName(query: String): Status<List<ListItem>>
}