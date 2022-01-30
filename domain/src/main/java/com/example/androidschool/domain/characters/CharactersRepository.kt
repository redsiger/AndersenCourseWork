package com.example.androidschool.domain.characters

import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.PagingAttr
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun getCharactersPaging(attr: PagingAttr): Status<List<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity>
}