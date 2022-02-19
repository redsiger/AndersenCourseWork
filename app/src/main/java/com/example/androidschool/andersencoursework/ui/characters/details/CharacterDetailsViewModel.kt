package com.example.androidschool.andersencoursework.ui.characters.details

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactor.CharacterDetailsInteractor
import com.example.androidschool.domain.episode.interactor.EpisodesListInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class CharacterDetailsViewModel constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val characterInteractor: CharacterDetailsInteractor,
    private val episodesListInteractor: EpisodesListInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState<CharacterDetailsState>>(UIState.InitialLoading)
    val uiState: StateFlow<UIState<CharacterDetailsState>> get() = _uiState.asStateFlow()

    fun load(characterId: Int) {
        _uiState.value = UIState.InitialLoading
        this.loadUIState(characterId)
    }

    fun refresh(characterId: Int) {
        _uiState.value = UIState.Refresh
        this.loadUIState(characterId)
    }

    fun retry(characterId: Int) {
        _uiState.value = UIState.InitialLoading
        this.loadUIState(characterId)
    }

    private fun loadUIState(characterId: Int) {
        viewModelScope.launch(defaultDispatcher) {

            when (val characterDetails = loadCharacterDetails(characterId)) {
                is NetworkResponse.Success -> {
                    val appearanceList = loadAppearance(characterDetails.data!!.appearance)
                    _uiState.value = UIState.Success(
                        data = CharacterDetailsState(
                            character = characterDetails.data!!,
                            appearance = appearanceList
                        )
                    )
                }
                is NetworkResponse.Error -> {
                    characterDetails.data?.let { character ->
                        val appearanceList = loadAppearance(character.appearance)
                        _uiState.value = UIState.Error(
                            data = CharacterDetailsState(
                                character = character,
                                appearance = appearanceList
                            ),
                            error = characterDetails.exception
                        )
                    } ?: run {
                        _uiState.value = UIState.EmptyError(
                            error = characterDetails.exception
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadCharacterDetails(characterId: Int): NetworkResponse<CharacterDetailsUI?> {
        val response = characterInteractor.getCharacterDetails(characterId)
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(
                data = mapper.mapCharacterDetails(response.data)
            )
            is NetworkResponse.Error -> NetworkResponse.Error(
                data = mapper.mapCharacterDetails(response.data),
                exception = response.exception
            )
        }
    }

    private suspend fun loadAppearance(appearanceList: List<Int>): List<EpisodeListItemUI> {
        val response = episodesListInteractor.getCharacterAppearance(appearanceList)
        return mapper.mapListEpisodeListItem(response.data)
    }

    class Factory @Inject constructor(
        @DispatcherIO
        private val defaultDispatcher: CoroutineDispatcher,
        private val characterInteractor: CharacterDetailsInteractor,
        private val episodesListInteractor: EpisodesListInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(
                defaultDispatcher,
                characterInteractor,
                episodesListInteractor,
                mapper
            ) as T
        }
    }
}

data class CharacterDetailsState(
    val character: CharacterDetailsUI,
    val appearance: List<EpisodeListItemUI>
)