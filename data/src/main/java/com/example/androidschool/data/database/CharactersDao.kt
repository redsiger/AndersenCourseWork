package com.example.androidschool.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidschool.data.database.model.CharacterRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterRoomEntity>)

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    suspend fun getCharactersPaging(limit: Int, offset: Int): List<CharacterRoomEntity>

    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterRoomEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%'")
    fun getSearchResults(query: String): Flow<List<CharacterRoomEntity>>

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}