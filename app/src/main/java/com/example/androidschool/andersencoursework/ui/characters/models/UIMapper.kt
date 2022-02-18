package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.andersencoursework.ui.core.recycler.ListItem
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.model.EpisodeListItem

class UIMapper {

    fun mapEpisodeListItemToListItemUI(entity: EpisodeListItem): ListItem.Item<EpisodeListItemUI> {
        return ListItem.Item(mapEpisodeListItem(entity))
    }

    fun mapEpisodeDetails(entity: EpisodeDetails?): EpisodeDetailsUI? {
        return if (entity != null) {
            EpisodeDetailsUI(
                airDate = entity.airDate,
                characters = entity.characters,
                episode = entity.episode,
                episodeId = entity.episodeId,
                season = entity.season,
                series = entity.series,
                title = entity.title
            )
        } else null
    }

    fun mapEpisodeListItem(entity: EpisodeListItem): EpisodeListItemUI {
        return EpisodeListItemUI(
            airDate = entity.airDate,
            characters = entity.characters,
            episode = entity.episode,
            episodeId = entity.episodeId,
            season = entity.season,
            series = entity.series,
            title = entity.title
        )
    }

    fun mapListEpisodeListItem(list: List<EpisodeListItem>): List<EpisodeListItemUI> {
        return list.map(::mapEpisodeListItem)
    }

    fun mapCharacterDetails(entity: CharacterDetails?): CharacterDetailsUI? {
        return if (entity != null) {
            CharacterDetailsUI(
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
        } else null
    }

    fun mapCharacterListItem(entity: CharacterListItem): CharacterListItemUI {
        return CharacterListItemUI(
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

    fun mapListCharacterListItem(list: List<CharacterListItem>): List<CharacterListItemUI> {
        return list.map(::mapCharacterListItem)
    }

    fun mapCharacterInEpisode(entity: CharacterInEpisode): CharacterListItemUI {
        return CharacterListItemUI(
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

    fun mapListCharacterInEpisode(list: List<CharacterInEpisode>): List<CharacterListItemUI> {
        return list.map(::mapCharacterInEpisode)
    }
}