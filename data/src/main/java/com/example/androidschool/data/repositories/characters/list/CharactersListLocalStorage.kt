package com.example.androidschool.data.repositories.characters.list

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersListDao
import com.example.androidschool.data.database.characters.model.CharacterInEpisodeRoom
import com.example.androidschool.data.database.characters.model.CharacterListItemRoom
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.search.model.ListItem

interface CharactersListLocalStorage {

    suspend fun searchCharacters(query: String): List<ListItem.CharacterListItem>

    suspend fun insertCharacters(characters: List<ListItem.CharacterListItem>, offset: Int)

    suspend fun getCharactersPaging(offset: Int, limit: Int): List<ListItem.CharacterListItem>

    suspend fun insertAndReturnPage(
        characters: List<ListItem.CharacterListItem>,
        offset: Int,
        limit: Int
    ): List<ListItem.CharacterListItem>

    suspend fun getCharactersInEpisode(): List<CharacterInEpisode>

    suspend fun insertAndReturnCharactersInEpisode(
        characters: List<CharacterInEpisode>
    ): List<CharacterInEpisode>

    class Base(
        private val dao: CharactersListDao,
        private val mapper: DatabaseMapper
    ) : CharactersListLocalStorage {

        override suspend fun searchCharacters(query: String): List<ListItem.CharacterListItem> =
            dao.searchCharacters(query).map(CharacterListItemRoom::toDomainModel)

        override suspend fun getCharactersPaging(offset: Int, limit: Int): List<ListItem.CharacterListItem> =
            dao.getCharactersPaging(offset).map(CharacterListItemRoom::toDomainModel)

        override suspend fun insertAndReturnPage(
            characters: List<ListItem.CharacterListItem>,
            offset: Int,
            limit: Int
        ): List<ListItem.CharacterListItem> {
            return dao
                .insertAndReturnPage(
                    characters.map { mapper.toRoomEntity(it, offset) }, offset, limit
                )
                .map(CharacterListItemRoom::toDomainModel)
        }

        override suspend fun getCharactersInEpisode(): List<CharacterInEpisode> =
            dao.getCharactersInEpisode().map(CharacterInEpisodeRoom::toDomainModel)

        override suspend fun insertAndReturnCharactersInEpisode(
            characters: List<CharacterInEpisode>
        ): List<CharacterInEpisode> =
            dao.insertAndReturnCharactersInEpisode(characters.map(mapper::toRoomEntity))
                .map(CharacterInEpisodeRoom::toDomainModel)

        override suspend fun insertCharacters(characters: List<ListItem.CharacterListItem>, offset: Int) =
            dao.insertCharacters(characters.map { mapper.toRoomEntity(it, offset) })

    }
}