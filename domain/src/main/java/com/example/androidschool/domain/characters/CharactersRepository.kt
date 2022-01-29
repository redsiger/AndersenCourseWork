package com.example.androidschool.domain.characters

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
    suspend fun getCharactersPaging(): LiveData<PagingData<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity>
}