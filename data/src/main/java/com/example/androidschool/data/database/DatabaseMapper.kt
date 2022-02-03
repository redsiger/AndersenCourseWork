package com.example.androidschool.data.database

import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.characters.model.CharactersRemoteKeys
import com.example.androidschool.data.database.episodes.model.EpisodeRoomEntity
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.domain.episode.model.EpisodeEntity

class DatabaseMapper {
    
    fun toRoomEntity(characterEntity: CharacterEntity): CharacterRoomEntity {
        return CharacterRoomEntity(
            appearance = characterEntity.appearance,
            betterCallSaulAppearance = characterEntity.betterCallSaulAppearance,
            birthday = characterEntity.birthday,
            category = characterEntity.category,
            charId = characterEntity.charId,
            img = characterEntity.img,
            name = characterEntity.name,
            nickname = characterEntity.nickname,
            occupation = characterEntity.occupation,
            portrayed = characterEntity.portrayed,
            status = characterEntity.status
        )
    }
    
    fun toRoomEntity(charactersEntityRemoteKeys: CharactersEntityRemoteKeys?): CharactersRemoteKeys? {
        return if (charactersEntityRemoteKeys == null) null
        else CharactersRemoteKeys(
            characterId = charactersEntityRemoteKeys.characterId,
            prevKey = charactersEntityRemoteKeys.prevKey,
            nextKey = charactersEntityRemoteKeys.nextKey
        )
    }
    
    fun toRoomEntity(episodeEntity: EpisodeEntity): EpisodeRoomEntity {
        return EpisodeRoomEntity(
            airDate = episodeEntity.airDate,
            characters = episodeEntity.characters,
            episode = episodeEntity.episode,
            episodeId = episodeEntity.episodeId,
            season = episodeEntity.season,
            series = episodeEntity.series,
            title = episodeEntity.title
        )
    }
}