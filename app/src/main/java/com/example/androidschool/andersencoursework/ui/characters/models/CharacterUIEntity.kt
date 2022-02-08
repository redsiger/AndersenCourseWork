package com.example.androidschool.andersencoursework.ui.characters.models

data class CharacterUIEntity(
    val charId: Int,
    val appearance: List<Int>,
    val betterCallSaulAppearance: List<Int>,
    val birthday: String,
    val category: String,
    val img: String,
    val name: String,
    val nickname: String,
    val occupation: List<String>,
    val portrayed: String,
    val status: String
)