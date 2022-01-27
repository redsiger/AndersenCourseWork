package com.example.androidschool.domain.characters

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status

interface CharactersRepository {
    suspend fun getCharactersPaging(): LiveData<PagingData<CharacterEntity>>
    suspend fun getCharacter(characterAttr: CharacterAttr): Status<List<CharacterEntity>>
}