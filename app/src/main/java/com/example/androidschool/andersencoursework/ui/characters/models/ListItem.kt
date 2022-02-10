package com.example.androidschool.andersencoursework.ui.characters.models

sealed class ListItem<T> {

    data class Item<T>(val character: T): ListItem<T>()
    data class Error<T>(val error: Exception): ListItem<T>()
    class Loading<T>: ListItem<T>()
}
