package com.example.androidschool.feature_characters.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidschool.feature_characters.data.room.model.CharacterRoomEntity

@Dao
interface CharactersDao {

    @Insert
    fun insertAll(characters: List<CharacterRoomEntity>)

    @Query("SELECT * FROM characters")
    fun getAll(): List<CharacterRoomEntity>

    @Query("DELETE FROM characters")
    fun deleteAll()
}