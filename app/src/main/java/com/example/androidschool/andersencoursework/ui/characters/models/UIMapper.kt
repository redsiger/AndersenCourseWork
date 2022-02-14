package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterListItem

class UIMapper {
    
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
        return ListItem.Item(
            CharacterListItemUI(
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
        )
    }
}