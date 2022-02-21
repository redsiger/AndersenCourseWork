package com.example.androidschool.andersencoursework.ui.seasons.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
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
    private val mapToListItemUI: (List<SeasonListItem>) -> List<SeasonListItemUI>,
    private val dispatcher: CoroutineDispatcher,
    private val interactor: SeasonsInteractor
): ViewModel() {

    private val _uiState = MutableStateFlow<Status<List<SeasonListItemUI>>>(Status.Initial)
    val uiState: StateFlow<Status<List<SeasonListItemUI>>> get() = _uiState.asStateFlow()

    fun load() {
        loadUIState()
    }

    fun retry() {
        _uiState.value = Status.Initial
    }

    private fun loadUIState() {

        viewModelScope.launch(dispatcher) {
            _uiState.value = interactor.getSeasons()
                .map(mapToListItemUI)
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
                mapper::mapListSeasonListItem,
                dispatcher,
                interactor
            ) as T
        }
    }
}