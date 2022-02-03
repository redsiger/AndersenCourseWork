package com.example.androidschool.data.database.characters

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.domain.characters.model.CharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersStorage(
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper
    ) {

    fun searchCharactersByNameOrNickname(query: String): Flow<List<CharacterEntity>> {
        return dao.searchCharactersByNameOrNickname(query).map { list ->
            list.map(CharacterRoomEntity::toDomainModel)
        }
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        dao.insertCharacters(characters.map { mapper.toRoomEntity(it) })
    }

    suspend fun insertCharacter(character: CharacterEntity) {
        dao.insertCharacter(mapper.toRoomEntity(character))
    }

    suspend fun getCharactersPaging(limit: Int, offset: Int): List<CharacterEntity> {
        return dao.getCharactersPaging(limit, offset).map(CharacterRoomEntity::toDomainModel)
    }
}