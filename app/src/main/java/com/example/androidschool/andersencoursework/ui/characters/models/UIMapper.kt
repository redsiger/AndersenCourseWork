package com.example.androidschool.andersencoursework.ui.characters.models

import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.domain.characters.model.CharacterEntity

class UIMapper {

    fun mapCharacterRoomEntity(roomEntity: CharacterRoomEntity): CharacterUIEntity {
        return CharacterUIEntity(
            appearance = roomEntity.appearance,
            betterCallSaulAppearance = roomEntity.betterCallSaulAppearance,
            birthday = roomEntity.birthday,
            category = roomEntity.category,
            charId = roomEntity.charId,
            img = roomEntity.img,
            name = roomEntity.name,
            nickname = roomEntity.nickname,
            occupation = roomEntity.occupation,
            portrayed = roomEntity.portrayed,
            status = roomEntity.status
        )
    }

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