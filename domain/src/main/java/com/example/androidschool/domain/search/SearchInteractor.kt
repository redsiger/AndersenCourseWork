package com.example.androidschool.domain.search

import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.ListItem
import com.example.androidschool.util.Status
import com.example.androidschool.util.flatten
import com.example.androidschool.util.merge

interface SearchInteractor {

    suspend fun getSearch(searchParameters: SearchParameters): Status<List<ListItem>>

    class Base(
        private val charactersRepository: CharactersListRepository,
        private val episodesRepository: EpisodesListRepository
    ) : SearchInteractor {

        override suspend fun getSearch(searchParameters: SearchParameters): Status<List<ListItem>> {

            val characters = charactersRepository.searchCharactersByNameOrNickName(searchParameters.query)
            val episodes = episodesRepository.searchEpisodesByNameOrAppearance(searchParameters.query)

            return characters.merge(episodes).flatten()
        }
    }
}