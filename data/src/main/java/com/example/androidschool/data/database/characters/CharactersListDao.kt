package com.example.androidschool.data.database.characters

import androidx.room.*
import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom
import com.example.androidschool.data.database.characters.model.CharacterInEpisodeRoom
import com.example.androidschool.data.database.characters.model.CharacterListItemRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersListDao {

    @Transaction
    suspend fun insertAndReturnPage(
        characters: List<CharacterListItemRoom>,
        offset: Int,
        limit: Int
    ): List<CharacterListItemRoom> {
        insertCharacters(characters)
        return getCharactersPaging(offset)
    }

    @Transaction
    suspend fun insertAndReturnCharacterInEpisode(
        character: CharacterInEpisodeRoom,
    ): CharacterInEpisodeRoom {
        insertCharacterDetails(character)
        return getCharacterInEpisode(character.charId)
    }

    @Transaction
    suspend fun insertAndReturnCharactersInEpisode(
        characters: List<CharacterInEpisodeRoom>,
    ): List<CharacterInEpisodeRoom> {
        insertCharactersInEpisode(characters)
        return getCharactersInEpisode()
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacterDetails(character: CharacterInEpisodeRoom)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharactersInEpisode(characters: List<CharacterInEpisodeRoom>)

    @Query("SELECT * FROM characters_in_episode WHERE charId = :id")
    suspend fun getCharacterInEpisode(id: Int): CharacterInEpisodeRoom

    @Query("SELECT * FROM characters_in_episode")
    suspend fun getCharactersInEpisode(): List<CharacterInEpisodeRoom>

    @Query("SELECT * FROM characters WHERE charId > :offset AND charId <= (:offset + :limit)")
    suspend fun getCharactersPagingStatus(offset: Int, limit: Int): List<CharacterListItemRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterListItemRoom>)

    @Query("SELECT * FROM characters WHERE character_offset = :offset")
    suspend fun getCharactersPaging(offset: Int): List<CharacterListItemRoom>

    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterListItemRoom>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%'")
    suspend fun searchCharacters(query: String): List<CharacterListItemRoom>

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}