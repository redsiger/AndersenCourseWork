package com.example.androidschool.data.database.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys

@Entity(tableName = "characters_remote_keys")
data class CharactersRemoteKeys(
    @PrimaryKey
    val characterId: Int,
    val prevKey: Int?,
    val nextKey: Int?
) {
    fun toDomainModel(): CharactersEntityRemoteKeys {
        return CharactersEntityRemoteKeys(
            characterId = characterId,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}
