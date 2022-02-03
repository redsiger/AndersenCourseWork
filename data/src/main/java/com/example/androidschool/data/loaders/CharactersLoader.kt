package com.example.androidschool.data.loaders

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.DefaultInternetException
import com.example.androidschool.util.Status

class CharactersLoader(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
) {

    suspend fun getCharacterPaging(limit: Int, offset: Int): Status<List<CharacterEntity>>
        = when (val characters = getRemote(limit, offset)) {
                is Status.Success -> {
                    dao.insertCharacters(characters.data.map(mapper::toRoomEntity))
                    characters
                }
                else -> {
                    getLocal(limit, offset)
                }
            }



    suspend fun getRemote(offset: Int, limit: Int): Status<List<CharacterEntity>> {
        return try {
            val response = service.getCharactersPaginated(offset, limit)
            if (response.isSuccessful) {
                val characters = (response.body()!! as List<CharacterNetworkEntity>)
                    .map(CharacterNetworkEntity::toDomainModel)
                Status.Success.Remote(characters)
            } else {
                val error = response.errorBody().toString()
                Status.Error(DefaultInternetException(error))
            }
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    suspend fun getLocal(limit: Int, offset: Int): Status<List<CharacterEntity>> {
        return Status.Success.Local(dao.getCharactersPaging(offset, limit).map{ it.toDomainModel() })
    }
}