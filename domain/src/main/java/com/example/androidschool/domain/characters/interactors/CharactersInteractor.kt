package com.example.androidschool.domain.characters.interactors

import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status

interface CharactersInteractor {

    suspend fun getCharactersPaging(): Status<List<CharacterEntity>>

    suspend fun getCharacter(characterAttr: CharacterAttr): Status<List<CharacterEntity>>

    class Base(private val repository: CharactersRepository): CharactersInteractor {

        override suspend fun getCharactersPaging(): Status<List<CharacterEntity>> {
            return repository.getCharactersPaging()
        }

        override suspend fun getCharacter(characterAttr: CharacterAttr): Status<List<CharacterEntity>> {
            return repository.getCharacter(characterAttr)
        }

    }

}