package com.example.androidschool.andersencoursework.ui.core.recycler

sealed class ListItem<T: DiffComparable> {

    data class Item<T: DiffComparable>(val item: T): ListItem<T>()
    data class Error<T: DiffComparable>(val error: Exception): ListItem<T>()
    class Loading<T: DiffComparable>: ListItem<T>()
}