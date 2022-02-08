package com.example.androidschool.data.database.characters

import androidx.room.*
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    fun getCharactersPagingStatus(offset: Int, limit: Int): List<CharacterRoomEntity>

    @Query("SELECT * FROM characters WHERE charId = :charId")
    suspend fun getCharacter(charId: Int): List<CharacterRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterRoomEntity>)

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    fun getCharactersPaging(offset: Int, limit: Int): List<CharacterRoomEntity>

    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterRoomEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%'")
    fun searchCharacters(query: String): Flow<List<CharacterRoomEntity>>

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}