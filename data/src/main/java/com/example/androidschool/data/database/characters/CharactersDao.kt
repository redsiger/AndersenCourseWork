package com.example.androidschool.data.database.characters

import androidx.paging.PagingSource
import androidx.room.*
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.characters.model.CharactersRemoteKeys
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    fun getCharactersPagingTest(offset: Int, limit: Int): Flow<List<CharacterRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterRoomEntity>)

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    fun getCharactersPaging(offset: Int, limit: Int): Flow<List<CharacterRoomEntity>>

    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterRoomEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%'")
    fun searchCharactersByNameOrNickname(query: String): Flow<List<CharacterRoomEntity>>

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharactersRemoteKeys(remoteKeys: List<CharactersRemoteKeys>)

    @Query("SELECT * FROM characters_remote_keys WHERE characterId = :characterId")
    suspend fun remoteKeysCharacters(characterId: Int): CharactersRemoteKeys?

    @Query("DELETE FROM characters_remote_keys")
    suspend fun clearAllRemoteKeys()

    @Transaction
    suspend fun clearCharactersWithRemoteKeys() {
        clearAllCharacters()
        clearAllRemoteKeys()
    }

    @Query("SELECT * FROM characters")
    fun getCharactersPagingForPagingSource(): PagingSource<Int, CharacterRoomEntity>
}