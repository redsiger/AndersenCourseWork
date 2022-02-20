package com.example.androidschool.data.repositories.characters.detail

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.repository.CharacterDetailsRepository
import com.example.androidschool.util.Status
import kotlin.Exception

class CharacterDetailsRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharacterDetailsLocalStorage
): CharacterDetailsRepository {

    override suspend fun getCharacterDetails(id: Int): Status<CharacterDetails> {
        return try {
            val response = service.getCharacter(id)
            if (response.isSuccessful) {
                val character = (response.body() as List<CharacterNetworkEntity>).first().toDomainDetailsModel()
                val data = localStorage.insertAndReturn(character)
                Status.Success(data)
            } else {
                val localCharacter = localStorage.getCharacterDetails(id)
                if (localCharacter != null) {
                    Status.Error(localCharacter, Exception(response.errorBody().toString()))
                } else Status.EmptyError
            }
        } catch (e: Exception) {
            val localCharacter = localStorage.getCharacterDetails(id)
            if (localCharacter != null) {
                Status.Error(localCharacter, e)
            } else Status.EmptyError
        }
    }
}