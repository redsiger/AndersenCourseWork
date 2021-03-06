package com.example.androidschool.data.database.characters

import androidx.room.*
import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom

@Dao
interface CharacterDetailsDao {

    @Query("SELECT * FROM characters_details WHERE charId = :id")
    suspend fun getCharacterDetails(id: Int): CharacterDetailsRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterDetails(characterDetails: CharacterDetailsRoom)

    @Transaction
    suspend fun insertAndReturn(characterDetails: CharacterDetailsRoom): CharacterDetailsRoom {
        insertCharacterDetails(characterDetails)
        return getCharacterDetails(characterDetails.charId)
    }
}