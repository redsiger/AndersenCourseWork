package com.example.androidschool.domain.search


data class SearchParameters(
    val query: String,
    val filter: List<ListItemType>
)

enum class ListItemType {
    CHARACTER,
    EPISODE
}