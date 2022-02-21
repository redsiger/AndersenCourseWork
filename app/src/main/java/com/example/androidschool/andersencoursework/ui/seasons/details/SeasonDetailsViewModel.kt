package com.example.androidschool.andersencoursework.ui.seasons.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.domain.episode.interactor.EpisodesListInteractor
import com.example.androidschool.domain.ListItem
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SeasonDetailsViewModel(
    private val mapToListItemUI: (List<ListItem.EpisodeListItem>) -> List<ListItemUI.EpisodeListItemUI>,
    private val dispatcher: CoroutineDispatcher,
    private val interactor: EpisodesListInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<Status<List<ListItemUI.EpisodeListItemUI>>>(Status.Initial)
    val uiState: StateFlow<Status<List<ListItemUI.EpisodeListItemUI>>> get() = _uiState.asStateFlow()

    fun load(season: String) {
        loadUIState(season)
    }

    fun retry() {
        _uiState.value = Status.Initial
    }

    private fun loadUIState(season: String) {

        viewModelScope.launch(dispatcher) {
            _uiState.value = interactor.getEpisodesBySeason(season)
                .map(mapToListItemUI)
        }
    }

    class Factory @Inject constructor(
        @DispatcherIO
        private val dispatcher: CoroutineDispatcher,
        private val interactor: EpisodesListInteractor,
        private val mapper: UIMapper
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SeasonDetailsViewModel::class.java)
            return SeasonDetailsViewModel(
                mapper::mapListEpisodeListItem,
                dispatcher,
                interactor
            ) as T
        }
    }
}