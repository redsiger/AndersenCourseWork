package com.example.androidschool.feature_characters.ui.model

import com.example.androidschool.feature_characters.domain.model.CharacterEntity

class CharacterUIMapper {

    fun fromDomain(domainEntity: CharacterEntity): CharacterUIEntity {
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