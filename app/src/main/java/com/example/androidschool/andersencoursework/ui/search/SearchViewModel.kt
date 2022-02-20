package com.example.androidschool.andersencoursework.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactor.CharactersListInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchViewModel(
    private val charactersListInteractor: CharactersListInteractor,
    private val mapper: UIMapper,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState<SearchState>>(UIState.InitialLoading)
    val uiState: StateFlow<UIState<SearchState>> get() = _uiState.asStateFlow()

    class Factory @Inject constructor(
        private val charactersListInteractor: CharactersListInteractor,
        private val mapper: UIMapper,
        @DispatcherIO
        private val dispatcher: CoroutineDispatcher
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SearchViewModel::class.java)
            return SearchViewModel(charactersListInteractor, mapper, dispatcher) as T
        }
    }
}

data class SearchState(
    val data: List<DiffComparable>
)

fun <T1, T2, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(0),
    transform: (T1, T2) -> R
): StateFlow<R> = combine(flow1, flow2) {
        o1, o2 -> transform.invoke(o1, o2)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value))