package com.example.androidschool.domain.search

import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.search.model.ListItem
import com.example.androidschool.util.Status
import com.example.androidschool.util.merge

interface SearchInteractor {

    suspend fun getSearch(query: String): Status<List<ListItem>>

    class Base(
        private val charactersRepository: CharactersListRepository,
        private val episodesRepository: EpisodesListRepository
    ) : SearchInteractor {

        override suspend fun getSearch(query: String): Status<List<ListItem>> {
            val characters = charactersRepository.searchCharactersByNameOrNickName(query)
            val episodes = episodesRepository.searchEpisodesByNameOrAppearance(query)

            return characters.merge(episodes, List<ListItem>::plus)
        }
    }
}