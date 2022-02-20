package com.example.androidschool.data.repositories.characters.list

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import kotlin.Exception

class CharactersListRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharactersListLocalStorage,
    ): CharactersListRepository {

    override suspend fun getCharactersInEpisode(charactersName: List<String>): Status<List<CharacterInEpisode>> {
        return try {
            // fetch all characters for saving in ROOM
            val response = service.getAllCharacters()
            if (response.isSuccessful) {
                val characters = (response.body() as List<CharacterNetworkEntity>)
                    .map(CharacterNetworkEntity::toDomainInEpisodeModel)

                // save all characters as CharacterInEpisode
                val charactersByName = localStorage.insertAndReturnCharactersInEpisode(characters)
                        // filter characters
                    .filter { charactersName.contains(it.name) }

                Status.Success(charactersByName)
            } else {
                val localCharactersByName = localStorage.getCharactersInEpisode()
                    .filter { charactersName.contains(it.name) }
                Status.Error(localCharactersByName, response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            val localCharactersByName = localStorage.getCharactersInEpisode()
                .filter { charactersName.contains(it.name) }
            Status.Error(localCharactersByName, e)
        }
    }

    override suspend fun getCharactersPagingState(
        offset: Int,
        limit: Int
    ): Status<List<CharacterListItem>> {
        return try {
            val response = service.getCharactersPaginated(offset, limit)
            if (response.isSuccessful) {
                val remoteData = response.body() as List<CharacterNetworkEntity>
                // insert new data in local storage
                val data = localStorage.insertAndReturnPage(
                    remoteData.map(CharacterNetworkEntity::toDomainModel),
                    offset, limit
                )
                Status.Success(data)
            }
            else {
                val localData = localStorage.getCharactersPaging(offset, limit)
                val exception = response.errorBody() as HttpException
                Status.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getCharactersPaging(offset, limit)
            Status.Error(localData, e)
        }
    }

    override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterListItem>>
        = localStorage.searchCharacters(query)
}