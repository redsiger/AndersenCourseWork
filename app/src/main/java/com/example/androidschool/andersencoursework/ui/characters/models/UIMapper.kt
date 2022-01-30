package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.domain.characters.model.CharacterEntity

class UIMapper {

    fun mapCharacterEntity(domainEntity: CharacterEntity): CharacterUIEntity {
        return CharacterUIEntity(
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