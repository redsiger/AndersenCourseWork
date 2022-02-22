package com.example.androidschool.andersencoursework.ui.search

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.ui.search.filter.SearchParametersUI
import com.example.androidschool.domain.search.SearchInteractor
import com.example.androidschool.domain.ListItem
import com.example.androidschool.domain.search.ListItemType
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val mapper: UIMapper,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _searchString = MutableStateFlow("")

    private val _filter = MutableStateFlow<List<ListItemType>>(listOf())

    private val _searchParameters: Flow<SearchParametersUI> = combine(
        _searchString,
        _filter
    ) { _searchQuery, _filter -> SearchParametersUI(_searchQuery, _filter) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = SearchParametersUI()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: Flow<SearchState<DiffComparable>> =
        _searchParameters
            .flatMapLatest { searchQuery ->
                flow {
                    when {
                        searchQuery.query.isBlank() -> emit(SearchState.Initial)
                        else -> {
                            emit(SearchState.Loading)
                            when (val response = getSearch(searchQuery)) {
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
            }
            .flowOn(dispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SearchState.Initial
            )

    fun search(query: String) {
        _searchString.value = query
        // reset filter when search string changed
        _filter.value = listOf()
    }

    val currentFilter get() = _filter.value

    fun filter(filter: List<ListItemType>) {
        _filter.value = filter
    }

    fun retry() {
        val currentFilter = _filter.value
        _filter.value = listOf()
        _filter.value = currentFilter
        val currentQuery = _searchString.value
        _searchString.value = ""
        _searchString.value = currentQuery
    }

    private suspend fun getSearch(filter: SearchParametersUI): Status<List<ListItem>> =
        searchInteractor.getSearch(filter.toDomainModel())

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