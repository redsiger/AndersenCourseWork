package com.example.androidschool.data.repositories.characters

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import kotlin.Exception


class CharactersRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharactersLocalStorage,
    ): CharactersRepository {

    override suspend fun getCharactersPagingState(
        offset: Int,
        limit: Int
    ): NetworkResponse<List<CharacterEntity>> {
        return try {
            val response = service.getCharactersPaginated(offset, limit)
            if (response.isSuccessful) {
                val remoteData = response.body() as List<CharacterNetworkEntity>
                localStorage.insertCharacters(remoteData.map(CharacterNetworkEntity::toDomainModel))
                NetworkResponse.Success(remoteData.map(CharacterNetworkEntity::toDomainModel))
            }
            else {
                val localData = localStorage.getCharactersPaging(offset, limit)
                val exception = response.errorBody() as HttpException
                NetworkResponse.Error(localData, Exception(exception))
            }
        } catch (e: Exception) {
            val localData = localStorage.getCharactersPaging(offset, limit)
            NetworkResponse.Error(localData, e)
        }
    }

    override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>>
        = localStorage.searchCharacters(query)

    override suspend fun getCharacter(charId: Int): NetworkResponse<CharacterEntity> {
        return try {
            val response = service.getCharacter(charId)
            if (response.isSuccessful) {
                val character = (response.body() as List<CharacterNetworkEntity>).first().toDomainModel()
                localStorage.insertCharacter(character)
                NetworkResponse.Success(character)
            } else {
                val localCharacter = localStorage.getCharacter(charId)
                NetworkResponse.Error(localCharacter, response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            val localCharacter = localStorage.getCharacter(charId)
            NetworkResponse.Error(localCharacter, e)
        } catch (e: Exception) {
            NetworkResponse.Error(CharacterEntity(), e)
        }
    }

    override suspend fun insertCharacters(characters: List<CharacterEntity>) {
        localStorage.insertCharacters(characters)
    }
}