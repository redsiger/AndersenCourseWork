package com.example.androidschool.domain.characters.repository

import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.util.Status

interface CharacterDetailsRepository {

    suspend fun getCharacterDetails(id: Int): Status<CharacterDetails>
}