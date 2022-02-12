package com.example.androidschool.data.database.characters

import androidx.room.*
import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom

@Dao
interface CharacterDetailsDao {

    @Query("SELECT * FROM characters_details WHERE charId = :id")
    suspend fun getCharacterDetails(id: Int): CharacterDetailsRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterDetails(characterRoomDetailEntity: CharacterDetailsRoom)

    @Transaction
    suspend fun insertAndReturn(characterRoomDetailEntity: CharacterDetailsRoom): CharacterDetailsRoom {
        insertCharacterDetails(characterRoomDetailEntity)
        return getCharacterDetails(characterRoomDetailEntity.charId)
    }
}