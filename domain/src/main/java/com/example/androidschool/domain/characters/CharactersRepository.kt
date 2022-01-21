package com.example.androidschool.domain.characters

import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status

interface CharactersRepository {
    suspend fun getCharactersPaging(): Status<List<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<List<CharacterEntity>>
}