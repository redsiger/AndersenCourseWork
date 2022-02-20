package com.example.androidschool.andersencoursework.ui.seasons.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.seasons.interactor.SeasonsInteractor
import com.example.androidschool.domain.seasons.model.SeasonListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SeasonsListViewModel(
    private val mapToListItemUI: (SeasonListItem) -> SeasonListItemUI,
    private val dispatcher: CoroutineDispatcher,
    private val interactor: SeasonsInteractor
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState<SeasonListState>>(UIState.InitialLoading)
    val uiState: StateFlow<UIState<SeasonListState>> get() = _uiState.asStateFlow()

    fun load() {
        _uiState.value = UIState.InitialLoading
        loadUIState()
    }

    fun refresh() {
        _uiState.value = UIState.Refresh
        loadUIState()
    }

    fun retry() {
        _uiState.value = UIState.InitialLoading
        loadUIState()
    }

    private fun loadUIState() {

        viewModelScope.launch(dispatcher) {
            when (val seasons = interactor.getSeasons()) {
                is Status.Success -> {
                    _uiState.value = UIState.Success(
                        SeasonListState(
                            seasons = seasons.data.map(mapToListItemUI)
                        )
                    )
                }
            }
        }
    }

    class Factory @Inject constructor (
        @DispatcherIO
        private val dispatcher: CoroutineDispatcher,
        private val interactor: SeasonsInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SeasonsListViewModel::class.java)
            return SeasonsListViewModel(
                mapper::mapSeasonListItem,
                dispatcher,
                interactor
            ) as T
        }
    }
}

data class SeasonListState(
    val seasons: List<SeasonListItemUI>
)