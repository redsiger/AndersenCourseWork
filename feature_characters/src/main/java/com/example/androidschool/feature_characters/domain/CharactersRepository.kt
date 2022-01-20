package com.example.androidschool.feature_characters.domain

import com.example.androidschool.feature_characters.domain.model.CharacterEntity

interface CharactersRepository {
    suspend fun getCharactersPaging(): List<CharacterEntity>
}