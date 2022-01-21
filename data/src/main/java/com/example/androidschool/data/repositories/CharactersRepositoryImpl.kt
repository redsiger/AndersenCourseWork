package com.example.androidschool.data.repositories

import com.example.androidschool.data.network.CharactersService
import com.example.androidschool.data.network.model.CharacterNetworkAttr
import com.example.androidschool.data.network.model.toDomainModel
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import retrofit2.HttpException
import kotlin.Exception

class CharactersRepositoryImpl(
    private val service: CharactersService
    ): CharactersRepository {

    private val pagingOffset = 10

    override suspend fun getCharactersPaging(): Status<List<CharacterEntity>> {
        return try {
            val response = service.getCharactersPaginated()
            if (response.isSuccessful) {
                Status.Success(response.body()!!.map { it.toDomainModel() })
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            return  Status.Error(e)
        }
    }

    override suspend fun getCharacter(characterAttr: CharacterAttr): Status<List<CharacterEntity>> {
        return try {
            val characterId = CharacterNetworkAttr(characterAttr.id).id
            val response = service.getCharacter(characterId)
            if (response.isSuccessful) {
                Status.Success(response.body()!!.map { it.toDomainModel() })
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }
}