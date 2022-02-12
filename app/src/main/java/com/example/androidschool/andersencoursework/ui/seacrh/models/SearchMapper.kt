package com.example.androidschool.andersencoursework.ui.seacrh.models

import com.example.androidschool.domain.characters.model.CharacterListItem

class SearchMapper {

    fun mapCharacterEntity(domainEntity: CharacterListItem): CharacterSearchEntity {
        return CharacterSearchEntity(
            appearance = domainEntity.appearance,
            betterCallSaulAppearance = domainEntity.betterCallSaulAppearance,
            birthday = domainEntity.birthday,
            category = domainEntity.category,
            charId = domainEntity.charId,
            img = domainEntity.img,
            name = domainEntity.name,
            nickname = domainEntity.nickname,
            occupation = domainEntity.occupation,
            portrayed = domainEntity.portrayed,
            status = domainEntity.status
        )
    }
}