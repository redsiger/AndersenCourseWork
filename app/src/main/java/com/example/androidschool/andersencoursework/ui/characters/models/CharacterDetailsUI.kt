package com.example.androidschool.andersencoursework.ui.characters.models

data class CharacterDetailsUI(
    val charId: Int = -1,
    val appearance: List<Int> = emptyList(),
    val betterCallSaulAppearance: List<Int> = emptyList(),
    val birthday: String = "",
    val category: String = "",
    val img: String = "",
    val name: String = "",
    val nickname: String = "",
    val occupation: List<String> = emptyList(),
    val portrayed: String = "",
    val status: String = "",
)