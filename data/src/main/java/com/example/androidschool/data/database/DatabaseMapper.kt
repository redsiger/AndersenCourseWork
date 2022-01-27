package com.example.androidschool.data.database

import com.example.androidschool.data.database.model.CharacterRoomEntity
import com.example.androidschool.domain.characters.model.CharacterEntity

class DatabaseMapper {
    
    fun mapCharacterEntity(characterEntity: CharacterEntity): CharacterRoomEntity {
        return CharacterRoomEntity(
            appearance = characterEntity.appearance,
            betterCallSaulAppearance = characterEntity.betterCallSaulAppearance,
            birthday = characterEntity.birthday,
            category = characterEntity.category,
            charId = characterEntity.charId,
            img = characterEntity.img,
            name = characterEntity.name,
            nickname = characterEntity.nickname,
            occupation = characterEntity.occupation,
            portrayed = characterEntity.portrayed,
            status = characterEntity.status
        )
    }
}