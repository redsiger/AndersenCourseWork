package com.example.androidschool.data.database

import com.example.androidschool.data.database.model.toDomainModel
import com.example.androidschool.domain.characters.model.CharacterEntity

class CharactersStorage(
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
    ) {

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