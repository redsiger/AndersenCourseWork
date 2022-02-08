package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersInteractor {

    suspend fun getCharactersPagingState(offset: Int, limit: Int): Status<List<CharacterEntity>>
    suspend fun getLocalCharactersPaging(offset: Int, limit: Int): List<CharacterEntity>


    suspend fun getRemoteCharactersPaging(offset: Int, limit: Int): Status<List<CharacterEntity>>
    suspend fun getLocalCharactersPagingMediator(offset: Int, limit: Int): Flow<List<CharacterEntity>>
    suspend fun getCharacter(charId: Int): Status<CharacterEntity>
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun clearCharactersWithRemoteKeys()
    suspend fun insertCharactersRemoteKeys(keys: List<CharactersEntityRemoteKeys>)
    suspend fun insertCharacters(characters: List<CharacterEntity>)
    suspend fun remoteKeysCharacters(charId: Int): CharactersEntityRemoteKeys?

    class Base(private val repository: CharactersRepository): CharactersInteractor {
        override suspend fun getCharactersPagingState(
            offset: Int,
            limit: Int
        ): Status<List<CharacterEntity>> {
            return repository.getCharactersPagingState(offset, limit)
        }

        override suspend fun getRemoteCharactersPaging(offset: Int, limit: Int): Status<List<CharacterEntity>>
            = repository.getRemoteCharactersPaging(offset, limit)

        override suspend fun getLocalCharactersPaging(offset: Int, limit: Int): List<CharacterEntity>
            = repository.getLocalCharactersPaging(offset, limit)

        override suspend fun getLocalCharactersPagingMediator(offset: Int, limit: Int): Flow<List<CharacterEntity>>
            = repository.getLocalCharactersPagingMediator(offset, limit)

        override suspend fun getCharacter(charId: Int): Status<CharacterEntity>
            = repository.getCharacter(charId)

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
            = repository.searchCharactersByNameOrNickName(query)

        override suspend fun clearCharactersWithRemoteKeys() = repository.clearCharactersWithRemoteKeys()

        override suspend fun insertCharactersRemoteKeys(keys: List<CharactersEntityRemoteKeys>)
            = repository.insertCharactersRemoteKeys(keys)

        override suspend fun insertCharacters(characters: List<CharacterEntity>)
            = repository.insertCharacters(characters)

        override suspend fun remoteKeysCharacters(charId: Int): CharactersEntityRemoteKeys?
            = repository.remoteKeysCharacters(charId)

    }

}