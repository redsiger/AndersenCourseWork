package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.andersencoursework.ui.core.recycler.ListItem
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.domain.episode.model.EpisodeListItem

class UIMapper {

    fun mapEpisodeListItemToListItemUI(entity: EpisodeListItem): ListItem.Item<EpisodeListItemUI> {
        return ListItem.Item(mapEpisodeListItem(entity))
    }

    fun mapEpisodeListItem(entity: EpisodeListItem): EpisodeListItemUI {
        return  EpisodeListItemUI(
            airDate = entity.airDate,
            characters = entity.characters,
            episode = entity.episode,
            episodeId = entity.episodeId,
            season = entity.season,
            series = entity.series,
            title = entity.title
        )
    }
    
    fun mapCharacterDetailEntity(entity: CharacterDetails): CharacterDetailsUI {
        return CharacterDetailsUI(
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

    fun mapCharacterEntity(entity: CharacterListItem): CharacterListItemUI {
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

    fun mapCharacterListItemToListItemUI(entity: CharacterListItem): ListItem.Item<CharacterListItemUI> {
        return ListItem.Item(mapCharacterEntity(entity))
    }
}