package com.example.androidschool.andersencoursework.ui.characters.models

sealed class ListItem {

    data class CharacterItem(val character: CharacterUIEntity): ListItem()
    data class Error(val error: Exception): ListItem()
    object Loading: ListItem()
}
