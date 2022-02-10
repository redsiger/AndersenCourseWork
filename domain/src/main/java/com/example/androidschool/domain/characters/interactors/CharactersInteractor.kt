package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersInteractor {

    suspend fun getCharactersPagingState(offset: Int, limit: Int): NetworkResponse<List<CharacterEntity>>
    suspend fun getCharacter(charId: Int): NetworkResponse<CharacterEntity?>
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    class Base(private val repository: CharactersRepository): CharactersInteractor {
        override suspend fun getCharactersPagingState(
            offset: Int,
            limit: Int
        ): NetworkResponse<List<CharacterEntity>>
            = repository.getCharactersPagingState(offset, limit)

        override suspend fun getCharacter(charId: Int): NetworkResponse<CharacterEntity?>
            = repository.getCharacter(charId)

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
            = repository.searchCharactersByNameOrNickName(query)

        override suspend fun insertCharacters(characters: List<CharacterEntity>)
            = repository.insertCharacters(characters)
    }

}