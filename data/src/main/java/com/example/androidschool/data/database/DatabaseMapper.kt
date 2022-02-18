package com.example.androidschool.data.database

import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom
import com.example.androidschool.data.database.characters.model.CharacterInEpisodeRoom
import com.example.androidschool.data.database.characters.model.CharacterListItemRoom
import com.example.androidschool.data.database.episodes.model.EpisodeDetailsRoom
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.model.EpisodeListItem

class DatabaseMapper {

    @JvmName("toRoomEntityInEpisode")
    fun toRoomEntity(listInEpisode: List<CharacterInEpisode>): List<CharacterInEpisodeRoom> =
        listInEpisode.map { toRoomEntity(it) }

    fun toRoomEntity(entity: CharacterInEpisode): CharacterInEpisodeRoom {
        return CharacterInEpisodeRoom(
            appearance = entity.appearance,
            betterCallSaulAppearance = entity.betterCallSaulAppearance,
            birthday = entity.birthday,
            category = entity.category,
            charId = entity.charId,
            img = entity.img,
            name = entity.name,
            nickname = entity.nickname,
            occupation = entity.occupation,
            portrayed = entity.portrayed,
            status = entity.status
        )
    }

    fun toRoomEntity(list: List<CharacterDetails>): List<CharacterDetailsRoom> =
        list.map { toRoomEntity(it) }

    fun toRoomEntity(entity: CharacterDetails): CharacterDetailsRoom {
        return CharacterDetailsRoom(
            appearance = entity.appearance,
            betterCallSaulAppearance = entity.betterCallSaulAppearance,
            birthday = entity.birthday,
            category = entity.category,
            charId = entity.charId,
            img = entity.img,
            name = entity.name,
            nickname = entity.nickname,
            occupation = entity.occupation,
            portrayed = entity.portrayed,
            status = entity.status
        )
    }

    fun toRoomEntity(entity: CharacterNetworkEntity, offset: Int): CharacterListItemRoom {
        return CharacterListItemRoom(
            appearance = entity.appearance,
            betterCallSaulAppearance = entity.betterCallSaulAppearance,
            birthday = entity.birthday,
            category = entity.category,
            charId = entity.charId,
            img = entity.img,
            name = entity.name,
            nickname = entity.nickname,
            occupation = entity.occupation,
            portrayed = entity.portrayed,
            status = entity.status,
            offset = offset
        )
    }

    fun toRoomEntity(entity: CharacterListItem, offset: Int): CharacterListItemRoom {
        return CharacterListItemRoom(
            appearance = entity.appearance,
            betterCallSaulAppearance = entity.betterCallSaulAppearance,
            birthday = entity.birthday,
            category = entity.category,
            charId = entity.charId,
            img = entity.img,
            name = entity.name,
            nickname = entity.nickname,
            occupation = entity.occupation,
            portrayed = entity.portrayed,
            status = entity.status,
            offset = offset
        )
    }

    fun toRoomEntity(entity: EpisodeListItem): EpisodeListItemRoom {
        return EpisodeListItemRoom(
            airDate = entity.airDate,
            characters = entity.characters,
            episode = entity.episode,
            episodeId = entity.episodeId,
            season = entity.season,
            series = entity.series,
            title = entity.title
        )
    }

    fun toRoomEntity(entity: EpisodeDetails): EpisodeDetailsRoom {
        return EpisodeDetailsRoom(
            airDate = entity.airDate,
            characters = entity.characters,
            episode = entity.episode,
            episodeId = entity.episodeId,
            season = entity.season,
            series = entity.series,
            title = entity.title
        )
    }
}