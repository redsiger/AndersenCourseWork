package com.example.androidschool.domain.characters

import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    suspend fun getCharactersPagingState(offset: Int, limit: Int): NetworkResponse<List<CharacterEntity>>

    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun getCharacter(charId: Int): NetworkResponse<CharacterEntity?>
    suspend fun insertCharacters(characters: List<CharacterEntity>)

}