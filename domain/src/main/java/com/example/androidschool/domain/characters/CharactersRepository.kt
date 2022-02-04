package com.example.androidschool.domain.characters

import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun getRemoteCharactersPaging(offset: Int, limit: Int): Status<List<CharacterEntity>>
    suspend fun getLocalCharactersPaging(offset: Int, limit: Int): Status<Flow<List<CharacterEntity>>>
    suspend fun getCharacter(charId: Int): Status<CharacterEntity>
    suspend fun remoteKeysCharacters(charId: Int): CharactersEntityRemoteKeys?
    suspend fun clearCharactersWithRemoteKeys()
    suspend fun insertCharactersRemoteKeys(keys: List<CharactersEntityRemoteKeys>)
    suspend fun insertCharacters(characters: List<CharacterEntity>)
    suspend fun getLocalCharactersPagingMediator(offset: Int, limit: Int): Flow<List<CharacterEntity>>
}