package com.example.androidschool.andersencoursework.ui.seasons.model

import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable

data class SeasonListItemUI(
    val season_number: Int,
    override val id: Int = season_number
): DiffComparable
