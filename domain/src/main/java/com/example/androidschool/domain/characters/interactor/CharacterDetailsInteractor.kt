package com.example.androidschool.domain.characters.interactor

import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.repository.CharacterDetailsRepository
import com.example.androidschool.util.NetworkResponse

interface CharacterDetailsInteractor {

    suspend fun getCharacterDetails(id: Int): NetworkResponse<CharacterDetails?>

    class Base(private val repository: CharacterDetailsRepository): CharacterDetailsInteractor {

        override suspend fun getCharacterDetails(id: Int): NetworkResponse<CharacterDetails?> =
            repository.getCharacterDetails(id)

    }
}