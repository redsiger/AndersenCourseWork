package com.example.androidschool.domain.seasons.repository

import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.util.Status

interface SeasonsRepository {

    suspend fun getSeasons(): Status<List<SeasonListItem>>

    suspend fun getSeasonsByAppearance(appearanceList: List<String>): Status<List<SeasonListItem>>

}