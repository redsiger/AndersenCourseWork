package com.example.androidschool.data.repositories.characters.detail

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.repository.CharacterDetailsRepository
import com.example.androidschool.util.NetworkResponse
import retrofit2.HttpException
import java.lang.Exception

class CharacterDetailsRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharacterDetailsLocalStorage
): CharacterDetailsRepository {

    override suspend fun getCharacterDetails(id: Int): NetworkResponse<CharacterDetails> {
        return try {
            val response = service.getCharacter(id)
            if (response.isSuccessful) {
                val character = (response.body() as List<CharacterNetworkEntity>).first().toDomainDetailsModel()
                val data = localStorage.insertAndReturn(character)
                NetworkResponse.Success(data)
            } else {
                val localCharacter = localStorage.getCharacterDetails(id)
                NetworkResponse.Error(localCharacter, response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            val localCharacter = localStorage.getCharacterDetails(id)
            NetworkResponse.Error(localCharacter, e)
        } catch (e: Exception) {
            NetworkResponse.Error(CharacterDetails(), e)
        }
    }
}