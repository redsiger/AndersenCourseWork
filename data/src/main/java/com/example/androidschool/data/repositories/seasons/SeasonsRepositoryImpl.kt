package com.example.androidschool.data.repositories.seasons

import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.domain.seasons.repository.SeasonsRepository
import com.example.androidschool.util.NetworkResponse

class SeasonsRepositoryImpl : SeasonsRepository {

    // Breaking bad API doesn't provide such functionality
    companion object {
        private val seasons = listOf(
            SeasonListItem("1"),
            SeasonListItem("2"),
            SeasonListItem("3"),
            SeasonListItem("4"),
            SeasonListItem("5")
        )
    }

    override suspend fun getSeasons(): NetworkResponse<List<SeasonListItem>> =
        NetworkResponse.Success(seasons)

    override suspend fun getSeasonsByAppearance(appearanceList: List<String>): NetworkResponse<List<SeasonListItem>> =
        NetworkResponse.Success(
            seasons.filter { appearanceList.contains(it.season.toString()) }
        )
}