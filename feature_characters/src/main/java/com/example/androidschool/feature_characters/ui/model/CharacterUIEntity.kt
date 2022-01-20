package com.example.androidschool.feature_characters.ui.model

import com.example.androidschool.feature_characters.domain.model.CharacterEntity

data class CharacterUIEntity(
    val appearance: List<Int>,
    val betterCallSaulAppearance: List<Int>,
    val birthday: String,
    val category: String,
    val charId: Int,
    val img: String,
    val name: String,
    val nickname: String,
    val occupation: List<String>,
    val portrayed: String,
    val status: String
)

fun CharacterUIEntity.fromDomain(domainEntity: CharacterEntity): CharacterUIEntity {
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