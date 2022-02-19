package com.example.androidschool.domain.listItemUtils

interface ItemFilter {
    fun filter(item: ListItem): Boolean
}