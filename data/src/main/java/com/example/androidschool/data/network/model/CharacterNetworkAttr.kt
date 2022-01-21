package com.example.androidschool.data.network.model

import com.example.androidschool.domain.characters.model.CharacterAttr

data class CharacterNetworkAttr(
    val id: Int
)

fun CharacterNetworkAttr.toDomainModel(): CharacterAttr = CharacterAttr(this.id)