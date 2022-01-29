package com.example.androidschool.data.database

import com.example.androidschool.data.database.model.toDomainModel
import com.example.androidschool.domain.characters.model.CharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersStorage(
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
    ) {

    fun searchCharactersByNameOrNickname(query: String): Flow<List<CharacterEntity>> {
        return dao.getSearchResults(query).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        dao.insertAll(characters.map { mapper.mapCharacterEntity(it) })
    }

    suspend fun insertCharacter(character: CharacterEntity) {
        dao.insertCharacter(mapper.mapCharacterEntity(character))
    }

    suspend fun getCharactersPaging(limit: Int, offset: Int): List<CharacterEntity> {
        return dao.getCharactersPaging(limit, offset).map { it.toDomainModel() }
    }
}