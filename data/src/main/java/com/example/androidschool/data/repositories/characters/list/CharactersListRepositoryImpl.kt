package com.example.androidschool.data.repositories.characters.list

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.ListItem
import com.example.androidschool.util.Status
import kotlin.Exception

class CharactersListRepositoryImpl(
    private val service: CharactersService,
    private val localStorage: CharactersListLocalStorage,
) : CharactersListRepository {

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
                Status.Error(localCharactersByName, Exception(response.errorBody().toString()))
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
    ): Status<List<ListItem.CharacterListItem>> {
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
            } else {
                val localData = localStorage.getCharactersPaging(offset, limit)
                val exception = Exception(response.errorBody().toString())
                Status.Error(localData, exception)
            }
        } catch (e: Exception) {
            val localData = localStorage.getCharactersPaging(offset, limit)
            Status.Error(localData, e)
        }
    }

    override suspend fun searchCharactersByNameOrNickName(query: String): Status<List<ListItem>> {
        return try {
            val response = service.getAllCharacters()
            if (response.isSuccessful) {
                (response.body() as List<CharacterNetworkEntity>)
                    .map { it.toDomainModel() }
                    // to save pagination
                    .chunked(10)
                    .forEachIndexed { index, list ->
                        localStorage.insertCharacters(list, index * 10)
                    }
                val searchResult = localStorage.searchCharacters(query)
                Status.Success(searchResult)
            } else {
                val searchResult = localStorage.searchCharacters(query)
                Status.Error(searchResult, Exception(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            val searchResult = localStorage.searchCharacters(query)
            Status.Error(searchResult, e)
        }
    }
}