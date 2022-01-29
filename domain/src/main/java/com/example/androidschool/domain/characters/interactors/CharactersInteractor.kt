package com.example.androidschool.domain.characters.interactors

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersInteractor {

    suspend fun getCharactersPaging(): LiveData<PagingData<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity>
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>

    class Base(private val repository: CharactersRepository): CharactersInteractor {

        override suspend fun getCharactersPaging(): LiveData<PagingData<CharacterEntity>> {
            return repository.getCharactersPaging()
        }

        override suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity> {
            return repository.getCharacter(characterAttr)
        }

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>> {
            return repository.searchCharactersByNameOrNickName(query)
        }

    }

}