package com.example.androidschool.data.repositories.seasons

import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.domain.seasons.repository.SeasonsRepository
import com.example.androidschool.util.NetworkResponse

class SeasonsRepositoryImpl: SeasonsRepository {

    // Breaking bad API doesn't provide such functionality
    override suspend fun getSeasons(): NetworkResponse<List<SeasonListItem>> =
        NetworkResponse.Success(
            listOf(
                SeasonListItem(1),
                SeasonListItem(2),
                SeasonListItem(3),
                SeasonListItem(4),
                SeasonListItem(5)
            )
        )
}