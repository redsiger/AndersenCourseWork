package com.example.androidschool.data.repositories.characters.list

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import kotlin.Exception


class CharactersListRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharactersLocalStorage,
    ): CharactersListRepository {

    override suspend fun getCharactersPagingState(
        offset: Int,
        limit: Int
    ): NetworkResponse<List<CharacterListItem>> {
        return try {
            val response = service.getCharactersPaginated(offset, limit)
            if (response.isSuccessful) {
                val remoteData = response.body() as List<CharacterNetworkEntity>
                // insert new data in local storage
                val data = localStorage.insertAndReturnPage(
                    remoteData.map(CharacterNetworkEntity::toDomainModel),
                    offset, limit
                )
                NetworkResponse.Success(data)
            }
            else {
                val localData = localStorage.getCharactersPaging(offset, limit)
                val exception = response.errorBody() as HttpException
                NetworkResponse.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getCharactersPaging(offset, limit)
            NetworkResponse.Error(localData, e)
        }
    }

    override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
        = localStorage.searchCharacters(query)
}