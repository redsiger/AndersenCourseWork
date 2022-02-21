package com.example.androidschool.andersencoursework.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.domain.search.SearchInteractor
import com.example.androidschool.domain.search.model.ListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val mapper: UIMapper,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: Flow<SearchState<DiffComparable>> =
        _searchQuery
            .flatMapLatest { query ->
                flow {
                    when {
                        query.isBlank() -> emit(SearchState.Initial)
                        else -> {
                            emit(SearchState.Loading)
                            when (val response = getSearch(query)) {
                                is Status.Initial -> emit(SearchState.Initial)
                                is Status.Success -> {
                                    if (response.data.isNotEmpty()) emit(
                                        SearchState.Success(
                                            response.extractData.map(mapper::mapListItem)
                                        )
                                    )
                                    else emit(SearchState.EmptySuccess)
                                }
                                is Status.Error -> {
                                    if (response.data.isNotEmpty()) emit(
                                        SearchState.Error(
                                            response.extractData.map(mapper::mapListItem)
                                        )
                                    )
                                    else emit(SearchState.EmptyError)
                                }
                                is Status.EmptyError -> emit(SearchState.EmptyError)
                            }
                        }
                    }

                }
                    .flowOn(dispatcher)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(0),
                        initialValue = SearchState.Initial
                    )
            }

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun retry() {
        val currentQuery = _searchQuery.value
        _searchQuery.value = ""
        _searchQuery.value = currentQuery
    }

    private suspend fun getSearch(query: String): Status<List<ListItem>> =
        searchInteractor.getSearch(query)

    class Factory @Inject constructor(
        private val searchInteractor: SearchInteractor,
        private val mapper: UIMapper,
        @DispatcherIO
        private val dispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SearchViewModel::class.java)
            return SearchViewModel(searchInteractor, mapper, dispatcher) as T
        }
    }
}

sealed class SearchState<out T> {

    data class Success<T>(val data: List<T>) : SearchState<T>()
    data class Error<T>(val data: List<T>) : SearchState<T>()
    object EmptyError : SearchState<Nothing>()
    object EmptySuccess : SearchState<Nothing>()
    object Initial : SearchState<Nothing>()
    object Loading : SearchState<Nothing>()
}