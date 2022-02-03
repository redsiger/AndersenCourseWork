package com.example.androidschool.data.repositories.characters

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.toDomainList
import com.example.androidschool.data.loaders.CharactersLoader
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import kotlin.Exception

typealias LoadCharactersAction = suspend (limit: Int, offset: Int) -> Status<List<CharacterEntity>>

class CharactersRepositoryImpl(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
    ): CharactersRepository {

    private val loader = CharactersLoader(service, dao, mapper)

    override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>> {
        return dao.searchCharactersByNameOrNickname(query).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun getRemoteCharactersPaging(offset: Int, limit: Int): Status<List<CharacterEntity>> {
        return loader.getRemote(offset, limit)
    }

    override suspend fun getLocalCharactersPaging(offset: Int, limit: Int): Status<List<CharacterEntity>> {
        return loader.getLocal(offset, limit)
    }

    override suspend fun getCharacter(charId: Int): Status<CharacterEntity> {
        return try {
            val response = service.getCharacter(charId)
            if (response.isSuccessful) {
                Status.Success.Remote(response.body()!!.map(CharacterNetworkEntity::toDomainModel).first())
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    override suspend fun remoteKeysCharacters(charId: Int): CharactersEntityRemoteKeys?
        = dao.remoteKeysCharacters(charId)?.toDomainModel()

    override suspend fun clearCharactersWithRemoteKeys() {
        dao.clearCharactersWithRemoteKeys()
    }

    override suspend fun insertCharactersRemoteKeys(keys: List<CharactersEntityRemoteKeys>) {
        dao.insertCharactersRemoteKeys(keys.map{mapper.toRoomEntity(it)!!})
    }

    override suspend fun insertCharacters(characters: List<CharacterEntity>) {
        dao.insertCharacters(characters.map(mapper::toRoomEntity))
    }

    override suspend fun getLocalCharactersPagingMediator(
        offset: Int,
        limit: Int
    ): List<CharacterEntity> {
        return dao.getCharactersPaging(offset, limit).toDomainList()
    }
}