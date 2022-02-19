package com.example.androidschool.domain.seasons.interactor

import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.domain.seasons.repository.SeasonsRepository
import com.example.androidschool.util.NetworkResponse

interface SeasonsInteractor {

    suspend fun getSeasons(): NetworkResponse<List<SeasonListItem>>

    suspend fun getSeasonsByAppearance(appearanceList: List<String>): NetworkResponse<List<SeasonListItem>>

    class Base(private val repository: SeasonsRepository) : SeasonsInteractor {

        override suspend fun getSeasons(): NetworkResponse<List<SeasonListItem>> =
            repository.getSeasons()

        override suspend fun getSeasonsByAppearance(appearanceList: List<String>): NetworkResponse<List<SeasonListItem>> =
            repository.getSeasonsByAppearance(appearanceList)
    }
}