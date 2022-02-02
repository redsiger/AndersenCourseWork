package com.example.androidschool.data.database.loaders

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.toDomainList
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.DefaultInternetException
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.*

class CharactersLoader(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper,
    private val onError: (message: String) -> Unit
) {

    suspend fun getCharacterPaging(limit: Int, offset: Int): Status<List<CharacterEntity>>
        =
            when (val characters = getRemote(limit, offset)) {
                is Status.Success -> {
                    dao.insertAll(characters.data.map(mapper::toRoomEntity))
                    getLocal(limit, offset)
                }
                is Status.Error -> {
                    onError(characters.exception.message ?: "DEFAULT_EXCEPTION")
                    getLocal(limit, offset)
                }
                else -> {
                    onError("DEFAULT_EXCEPTION")
                    getLocal(limit, offset)
                }
            }



    private suspend fun getRemote(limit: Int, offset: Int): Status<List<CharacterEntity>> {
        return try {
            val response = service.getCharactersPaginated(limit, offset)
            if (response.isSuccessful) {
                val characters = (response.body()!! as List<CharacterNetworkEntity>)
                    .map(CharacterNetworkEntity::toDomainModel)
                Status.Success(characters)
            } else {
                val error = response.errorBody().toString()
                Status.Error(DefaultInternetException(error))
            }
        } catch (e: Exception) {
            Status.Error(DefaultInternetException(e.message!!))
        }
    }

    private suspend fun getLocal(limit: Int, offset: Int): Status<List<CharacterEntity>> {
        return Status.Success(dao.getCharactersPaging(limit, offset).map{ it.toDomainModel() })
    }
}