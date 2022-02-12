package com.example.androidschool.data.database

import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.episodes.model.EpisodeRoomEntity
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.domain.episode.model.EpisodeEntity

class DatabaseMapper {
    
    fun toRoomEntity(characterDetailEntity: CharacterDetails): CharacterDetailsRoom {
        return CharacterDetailsRoom(
            appearance = characterDetailEntity.appearance,
            betterCallSaulAppearance = characterDetailEntity.betterCallSaulAppearance,
            birthday = characterDetailEntity.birthday,
            category = characterDetailEntity.category,
            charId = characterDetailEntity.charId,
            img = characterDetailEntity.img,
            name = characterDetailEntity.name,
            nickname = characterDetailEntity.nickname,
            occupation = characterDetailEntity.occupation,
            portrayed = characterDetailEntity.portrayed,
            status = characterDetailEntity.status
        )
    }

    fun toRoomEntity(characterNetworkEntity: CharacterNetworkEntity, offset: Int): CharacterRoomEntity {
        return CharacterRoomEntity(
            appearance = characterNetworkEntity.appearance,
            betterCallSaulAppearance = characterNetworkEntity.betterCallSaulAppearance,
            birthday = characterNetworkEntity.birthday,
            category = characterNetworkEntity.category,
            charId = characterNetworkEntity.charId,
            img = characterNetworkEntity.img,
            name = characterNetworkEntity.name,
            nickname = characterNetworkEntity.nickname,
            occupation = characterNetworkEntity.occupation,
            portrayed = characterNetworkEntity.portrayed,
            status = characterNetworkEntity.status,
            offset = offset
        )
    }

    fun toRoomEntity(characterEntity: CharacterListItem, offset: Int): CharacterRoomEntity {
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
            status = characterEntity.status,
            offset = offset
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