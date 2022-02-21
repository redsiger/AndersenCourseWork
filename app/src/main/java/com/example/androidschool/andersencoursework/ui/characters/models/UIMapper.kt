package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.episode.model.EpisodeDetails
import com.example.androidschool.domain.search.model.ListItem
import com.example.androidschool.domain.seasons.model.SeasonListItem

class UIMapper {

    fun mapListItem(item: ListItem): ListItemUI {
        return when (item) {
            is ListItem.EpisodeListItem -> mapListItemEpisode(item)
            is ListItem.CharacterListItem -> mapListItemCharacter(item)
        }
    }


    private fun mapListItemCharacter(item: ListItem.CharacterListItem): ListItemUI.CharacterListItemUI =
        ListItemUI.CharacterListItemUI(
            charId = item.charId,
            appearance = item.appearance,
            betterCallSaulAppearance = item.betterCallSaulAppearance,
            birthday = item.birthday,
            category = item.category,
            img = item.img,
            name = item.name,
            nickname = item.nickname,
            occupation = item.occupation,
            portrayed = item.portrayed,
            status = item.status
        )

    private fun mapListItemEpisode(item: ListItem.EpisodeListItem): ListItemUI.EpisodeListItemUI =
        ListItemUI.EpisodeListItemUI(
            airDate = item.airDate,
            characters = item.characters,
            episode = item.episode,
            episodeId = item.episodeId,
            season = item.season,
            series = item.series,
            title = item.title
        )

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

    fun mapEpisodeListItem(entity: ListItem.EpisodeListItem): ListItemUI.EpisodeListItemUI {
        return ListItemUI.EpisodeListItemUI(
            airDate = entity.airDate,
            characters = entity.characters,
            episode = entity.episode,
            episodeId = entity.episodeId,
            season = entity.season,
            series = entity.series,
            title = entity.title
        )
    }

    fun mapListEpisodeListItem(list: List<ListItem.EpisodeListItem>): List<ListItemUI.EpisodeListItemUI> =
        list.map(::mapEpisodeListItem)

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

    fun mapCharacterListItem(entity: ListItem.CharacterListItem): ListItemUI.CharacterListItemUI {
        return ListItemUI.CharacterListItemUI(
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

    private fun mapCharacterInEpisode(entity: CharacterInEpisode): ListItemUI.CharacterListItemUI {
        return ListItemUI.CharacterListItemUI(
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

    fun mapListCharacterInEpisode(list: List<CharacterInEpisode>): List<ListItemUI.CharacterListItemUI> =
        list.map(::mapCharacterInEpisode)
}