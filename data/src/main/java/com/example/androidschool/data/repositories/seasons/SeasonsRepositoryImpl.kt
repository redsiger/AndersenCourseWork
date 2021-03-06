package com.example.androidschool.data.repositories.seasons

import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.domain.seasons.repository.SeasonsRepository
import com.example.androidschool.util.Status

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

    override suspend fun getSeasons(): Status<List<SeasonListItem>> =
        Status.Success(seasons)

    override suspend fun getSeasonsByAppearance(appearanceList: List<String>): Status<List<SeasonListItem>> =
        Status.Success(
            seasons.filter { appearanceList.contains(it.season.toString()) }
        )
}