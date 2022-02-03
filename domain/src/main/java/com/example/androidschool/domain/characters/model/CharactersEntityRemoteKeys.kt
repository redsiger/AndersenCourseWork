package com.example.androidschool.domain.characters.model

data class CharactersEntityRemoteKeys(
    val characterId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
