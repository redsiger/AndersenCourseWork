package com.example.androidschool.andersencoursework.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.ui.search.models.CharacterSearchEntity
import com.example.androidschool.andersencoursework.ui.search.models.SearchMapper
import com.example.androidschool.domain.characters.interactor.CharactersListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchViewModel(
    private val charactersListInteractor: CharactersListInteractor
): ViewModel() {

    private val mapper = SearchMapper()
    private val coroutineContext = Dispatchers.IO

    fun getSearchResults(query: String): Flow<List<CharacterSearchEntity>> {
        return charactersListInteractor
            .searchCharactersByNameOrNickName(query)
            .map { list ->
                list.map { mapper.mapCharacterEntity(it) }
            }
            .flowOn(coroutineContext)
    }

    class Factory @Inject constructor(
        private val charactersListInteractor: CharactersListInteractor
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SearchViewModel::class.java)
            return SearchViewModel(charactersListInteractor) as T
        }

    }
}