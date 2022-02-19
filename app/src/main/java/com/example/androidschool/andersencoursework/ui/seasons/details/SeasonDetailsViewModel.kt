package com.example.androidschool.andersencoursework.ui.seasons.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.episode.interactor.EpisodesListInteractor
import com.example.androidschool.domain.episode.model.EpisodeListItem
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SeasonDetailsViewModel(
    private val mapToListItemUI: (EpisodeListItem) -> EpisodeListItemUI,
    private val dispatcher: CoroutineDispatcher,
    private val interactor: EpisodesListInteractor
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState<SeasonDetailsState>>(UIState.InitialLoading)
    val uiState: StateFlow<UIState<SeasonDetailsState>> get() = _uiState.asStateFlow()

    fun load(season: String) {
        _uiState.value = UIState.InitialLoading
        loadUIState(season)
    }

    fun refresh(season: String) {
        _uiState.value = UIState.Refresh
        loadUIState(season)
    }

    fun retry(season: String) {
        _uiState.value = UIState.InitialLoading
        loadUIState(season)
    }

    private fun loadUIState(season: String) {

        viewModelScope.launch(dispatcher) {
            when (val episodes = interactor.getEpisodesBySeason(season)) {
                is NetworkResponse.Success -> {
                    _uiState.value = UIState.Success(
                        SeasonDetailsState(
                            episodes = episodes.data.map(mapToListItemUI)
                        )
                    )
                }
            }
        }
    }

    class Factory @Inject constructor (
        @DispatcherIO
        private val dispatcher: CoroutineDispatcher,
        private val interactor: EpisodesListInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SeasonDetailsViewModel::class.java)
            return SeasonDetailsViewModel(
                mapper::mapEpisodeListItem,
                dispatcher,
                interactor
            ) as T
        }
    }
}

data class SeasonDetailsState(
    val episodes: List<EpisodeListItemUI>
)