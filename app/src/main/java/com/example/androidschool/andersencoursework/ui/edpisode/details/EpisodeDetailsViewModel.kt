package com.example.androidschool.andersencoursework.ui.edpisode.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactor.CharactersListInteractor
import com.example.androidschool.domain.episode.interactor.EpisodeDetailsInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val charactersListInteractor: CharactersListInteractor,
    private val episodeDetailsInteractor: EpisodeDetailsInteractor,
    private val mapper: UIMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<EpisodeDetailsState>>(UIState.InitialLoading)
    val uiState: StateFlow<UIState<EpisodeDetailsState>> get() = _uiState.asStateFlow()

    fun load(episodeId: Int) {
        _uiState.value = UIState.InitialLoading
        loadUIState(episodeId)
    }

    fun refresh(episodeId: Int) {
        _uiState.value = UIState.Refresh
        loadUIState(episodeId)
    }

    fun retry(episodeId: Int) {
        _uiState.value = UIState.InitialLoading
        loadUIState(episodeId)
    }

    private fun loadUIState(episodeId: Int) {

        viewModelScope.launch(defaultDispatcher) {
            when (val episodeDetails = loadEpisodeDetails(episodeId)) {
                is NetworkResponse.Success -> {
                    val appearanceList = loadCharacters(episodeDetails.data!!.characters)
                    _uiState.value = UIState.Success(
                        data = EpisodeDetailsState(
                            episode = episodeDetails.data!!,
                            characters = appearanceList
                        )
                    )
                }
                is NetworkResponse.Error -> {
                    episodeDetails.data?.let { episode ->
                        val appearanceList = loadCharacters(episode.characters)
                        _uiState.value = UIState.Error(
                            data = EpisodeDetailsState(
                                episode = episode,
                                characters = appearanceList
                            ),
                            error = episodeDetails.exception
                        )
                    } ?: run {
                        _uiState.value = UIState.EmptyError(
                            error = episodeDetails.exception
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadEpisodeDetails(characterId: Int): NetworkResponse<EpisodeDetailsUI?> {
        val response = episodeDetailsInteractor.getEpisodeDetails(characterId)
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(
                data = mapper.mapEpisodeDetails(response.data)
            )
            is NetworkResponse.Error -> NetworkResponse.Error(
                data = mapper.mapEpisodeDetails(response.data),
                exception = response.exception
            )
        }
    }

    private suspend fun loadCharacters(charactersIdList: List<String>): List<CharacterListItemUI> {
        val response = charactersListInteractor.getCharactersInEpisode(charactersIdList)
        return mapper.mapListCharacterInEpisode(response.data)
    }

    class Factory @Inject constructor(
        @DispatcherIO
        private val defaultDispatcher: CoroutineDispatcher,
        private val charactersListInteractor: CharactersListInteractor,
        private val episodeDetailsInteractor: EpisodeDetailsInteractor,
        private val mapper: UIMapper
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodeDetailsViewModel::class.java)
            return EpisodeDetailsViewModel(
                defaultDispatcher,
                charactersListInteractor,
                episodeDetailsInteractor,
                mapper
            ) as T
        }
    }
}

data class EpisodeDetailsState(
    val episode: EpisodeDetailsUI,
    val characters: List<CharacterListItemUI>
)