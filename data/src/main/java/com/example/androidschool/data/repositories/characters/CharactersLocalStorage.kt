package com.example.androidschool.data.repositories.characters

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.characters.model.toDomainList
import com.example.androidschool.domain.characters.model.CharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CharactersLocalStorage {

    fun searchCharacters(query: String): Flow<List<CharacterEntity>>
    suspend fun insertCharacters(characters: List<CharacterEntity>)
    suspend fun getCharactersPaging(offset: Int, limit: Int): List<CharacterEntity>

    suspend fun getCharacter(charId: Int): CharacterEntity
    suspend fun insertCharacter(character: CharacterEntity)

    class Base(
        private val dao: CharactersDao,
        private val mapper: DatabaseMapper
    ): CharactersLocalStorage {

        override fun searchCharacters(query: String): Flow<List<CharacterEntity>>
            = dao.searchCharacters(query).map { it.toDomainList() }

        override suspend fun getCharactersPaging(offset: Int, limit: Int): List<CharacterEntity>
            = dao.getCharactersPaging(offset, limit).map(CharacterRoomEntity::toDomainModel)

        override suspend fun getCharacter(charId: Int): CharacterEntity
            = dao.getCharacter(charId).first().toDomainModel()

        override suspend fun insertCharacter(character: CharacterEntity)
            = dao.insertCharacter(mapper.toRoomEntity(character))

        override suspend fun insertCharacters(characters: List<CharacterEntity>)
            = dao.insertCharacters(characters.map(mapper::toRoomEntity))

    }
}