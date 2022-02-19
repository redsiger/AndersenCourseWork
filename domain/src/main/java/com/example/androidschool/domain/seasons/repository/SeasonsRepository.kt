package com.example.androidschool.domain.seasons.repository

import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.util.NetworkResponse

interface SeasonsRepository {

    suspend fun getSeasons(): NetworkResponse<List<SeasonListItem>>

    suspend fun getSeasonsByAppearance(appearanceList: List<String>): NetworkResponse<List<SeasonListItem>>

}