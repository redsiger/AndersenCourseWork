package com.example.androidschool.data.database.loaders

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersLoader(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
) {

    suspend fun getCharacterPaging(limit: Int, offset: Int): Status<List<CharacterEntity>>  {
        return try {
            when (val characters = getRemote(limit, offset)) {
                is Status.Success -> {
                    dao.insertAll(characters.data.map(mapper::toRoomEntity))
                    characters
                }
                is Status.Error -> {
                    getLocal(limit, offset)
                }
                else -> getLocal(limit, offset)
            }
        } catch (e: Exception) {
            Status.Error(e)
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
                Status.Error(Exception(error))
            }
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    private suspend fun getLocal(limit: Int, offset: Int): Status<List<CharacterEntity>> {
        return try {
            val charactersLocal = dao.getCharactersPaging(limit, offset)
            Status.Success(charactersLocal.map(CharacterRoomEntity::toDomainModel))
        } catch (e: java.lang.Exception) {
            Status.Error(e)
        }
    }
}