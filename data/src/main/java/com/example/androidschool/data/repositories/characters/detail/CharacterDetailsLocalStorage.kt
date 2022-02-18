package com.example.androidschool.data.repositories.characters.detail

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharacterDetailsDao
import com.example.androidschool.domain.characters.model.CharacterDetails
import kotlin.Exception

interface CharacterDetailsLocalStorage {

    suspend fun getCharacterDetails(id: Int): CharacterDetails?

    suspend fun insertAndReturn(character: CharacterDetails): CharacterDetails

    class Base(
        private val dao: CharacterDetailsDao,
        private val mapper: DatabaseMapper
    ): CharacterDetailsLocalStorage {

        override suspend fun getCharacterDetails(id: Int): CharacterDetails? {
            return try {
                dao.getCharacterDetails(id).toDomainModel()
            } catch (e: Exception) { null }
        }

        override suspend fun insertAndReturn(character: CharacterDetails): CharacterDetails {
            return dao.insertAndReturn(
                mapper.toRoomEntity(character)
            ).toDomainModel()
        }
    }
}