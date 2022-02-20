package com.example.androidschool.domain.characters.model

import com.example.androidschool.domain.search.model.SearchItem
import com.example.androidschool.domain.search.model.TYPE_CHARACTER

data class CharacterListItem(
    val appearance: List<Int> = emptyList(),
    val betterCallSaulAppearance: List<Int> = emptyList(),
    val birthday: String = "",
    val category: String = "",
    val charId: Int = -1,
    val img: String = "",
    val name: String = "",
    val nickname: String = "",
    val occupation: List<String> = emptyList(),
    val portrayed: String = "",
    val status: String = "",
    override val type: String = TYPE_CHARACTER,
): SearchItem