package com.example.androidschool.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidschool.data.database.model.CharacterRoomEntity

@Dao
interface CharactersDao {

    @Insert
    fun insertAll(characters: List<com.example.androidschool.data.database.model.CharacterRoomEntity>)

    @Query("SELECT * FROM characters")
    fun getAll(): List<com.example.androidschool.data.database.model.CharacterRoomEntity>

    @Query("DELETE FROM characters")
    fun deleteAll()
}