package com.example.androidschool.andersencoursework.ui.search.filter

import com.example.androidschool.domain.search.ListItemType
import com.example.androidschool.domain.search.SearchParameters

data class SearchParametersUI(
    val query: String = "",
    val filter: List<ListItemType> = listOf()
) {
    fun toDomainModel(): SearchParameters =
        SearchParameters(query = query, filter = filter)
}
