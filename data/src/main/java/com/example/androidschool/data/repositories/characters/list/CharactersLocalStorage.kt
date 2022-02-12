package com.example.androidschool.data.repositories.characters.list

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersListDao
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.characters.model.toDomainList
import com.example.androidschool.domain.characters.model.CharacterListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CharactersLocalStorage {

    fun searchCharacters(query: String): Flow<List<CharacterListItem>>
    suspend fun insertCharacters(characters: List<CharacterListItem>, offset: Int)
    suspend fun getCharactersPaging(offset: Int, limit: Int): List<CharacterListItem>

    suspend fun insertAndReturnPage(
        characters: List<CharacterListItem>,
        offset: Int,
        limit: Int
    ): List<CharacterListItem>

    class Base(
        private val dao: CharactersListDao,
        private val mapper: DatabaseMapper
    ): CharactersLocalStorage {

        override fun searchCharacters(query: String): Flow<List<CharacterListItem>>
            = dao.searchCharacters(query).map { it.toDomainList() }

        override suspend fun getCharactersPaging(offset: Int, limit: Int): List<CharacterListItem>
            = dao.getCharactersPaging(offset).map(CharacterRoomEntity::toDomainModel)

        override suspend fun insertAndReturnPage(
            characters: List<CharacterListItem>,
            offset: Int,
            limit: Int
        ): List<CharacterListItem> {
            return dao
                .insertAndReturnPage(
                    characters.map { mapper.toRoomEntity(it, offset) }
                    , offset, limit
                )
                .map(CharacterRoomEntity::toDomainModel)
        }

        override suspend fun insertCharacters(characters: List<CharacterListItem>, offset: Int)
            = dao.insertCharacters(characters.map { mapper.toRoomEntity(it, offset) })

    }
}