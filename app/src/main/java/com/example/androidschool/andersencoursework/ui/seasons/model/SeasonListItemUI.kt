package com.example.androidschool.andersencoursework.ui.seasons.model

import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable

data class SeasonListItemUI(
    val season: String,
    override val id: Int = season.toInt()
): DiffComparable
