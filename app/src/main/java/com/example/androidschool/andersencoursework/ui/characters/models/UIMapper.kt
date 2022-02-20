package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.domain.seasons.model.SeasonListItem

class UIMapper {

    private fun mapSeasonListItem(entity: SeasonListItem): SeasonListItemUI =
        SeasonListItemUI(season = entity.season)

    fun mapListSeasonListItem(list: List<SeasonListItem>): List<SeasonListItemUI> =
        list.map(::mapSeasonListItem)

    fun mapEpisodeDetails(entity: EpisodeDetails): EpisodeDetailsUI {
        return EpisodeDetailsUI(
                airDate = entity.airDate,
                characters = entity.characters,
                episode = entity.episode,
                episodeId = entity.episodeId,
                season = entity.season,
                series = entity.series,
                title = entity.title
            )
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

    fun mapCharacterDetails(entity: CharacterDetails): CharacterDetailsUI {
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

    private fun mapCharacterInEpisode(entity: CharacterInEpisode): CharacterListItemUI {
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

    fun mapListCharacterInEpisode(list: List<CharacterInEpisode>): List<CharacterListItemUI> =
        list.map(::mapCharacterInEpisode)
}