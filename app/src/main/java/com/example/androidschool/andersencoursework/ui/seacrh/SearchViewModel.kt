package com.example.androidschool.andersencoursework.ui.seacrh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.seacrh.models.CharacterSearchEntity
import com.example.androidschool.andersencoursework.ui.seacrh.models.SearchMapper
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchViewModel(
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = SearchMapper()
    private val coroutineContext = Dispatchers.IO

    fun getSearchResults(query: String): Flow<List<CharacterSearchEntity>> {
        return charactersInteractor
            .searchCharactersByNameOrNickName(query)
            .map { list ->
                list.map { mapper.mapCharacterEntity(it) }
            }
            .flowOn(coroutineContext)
    }

    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SearchViewModel::class.java)
            return SearchViewModel(charactersInteractor) as T
        }

    }
}