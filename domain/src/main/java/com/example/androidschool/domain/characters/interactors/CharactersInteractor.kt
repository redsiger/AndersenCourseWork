package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.PagingAttr
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersInteractor {

    suspend fun getCharactersPaging(attr: PagingAttr): Status<List<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity>
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>

    class Base(private val repository: CharactersRepository): CharactersInteractor {

        override suspend fun getCharactersPaging(attr: PagingAttr): Status<List<CharacterEntity>> {
            return repository.getCharactersPaging(attr)
        }

        override suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity> {
            return repository.getCharacter(characterAttr)
        }

        override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>> {
            return repository.searchCharactersByNameOrNickName(query)
        }

    }

}