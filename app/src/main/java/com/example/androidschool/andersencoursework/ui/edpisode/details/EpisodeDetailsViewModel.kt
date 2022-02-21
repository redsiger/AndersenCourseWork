package com.example.androidschool.andersencoursework.ui.edpisode.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.domain.characters.interactor.CharactersListInteractor
import com.example.androidschool.domain.episode.interactor.EpisodeDetailsInteractor
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val charactersListInteractor: CharactersListInteractor,
    private val episodeDetailsInteractor: EpisodeDetailsInteractor,
    private val mapper: UIMapper
) : ViewModel() {

    private val _episodeDetails = MutableStateFlow<Status<EpisodeDetailsUI>>(Status.Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _charactersInEpisode: Flow<Status<List<ListItemUI.CharacterListItemUI>>> =
        _episodeDetails.flatMapLatest { episode ->
            flowOf(
                when (episode) {
                    is Status.Initial -> Status.Initial
                    is Status.EmptyError -> Status.EmptyError
                    else -> loadCharactersInEpisode(episode.extractData.characters)
                }
            )
                .flowOn(defaultDispatcher)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(0),
                    initialValue = Status.Initial
                )
        }

    val uiState: Flow<EpisodeDetailsState> =
        combine(_episodeDetails, _charactersInEpisode, ::makeState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(0),
                initialValue = EpisodeDetailsState(
                    episode = Status.Initial,
                    characters = Status.Initial
                )
            )

    private fun makeState(
        episode: Status<EpisodeDetailsUI>,
        characters: Status<List<ListItemUI.CharacterListItemUI>>
    ): EpisodeDetailsState = EpisodeDetailsState(episode, characters)

    fun load(episodeId: Int) {
        loadEpisode(episodeId)
    }

    fun retry() {
        _episodeDetails.value = Status.Initial
    }

    private fun loadEpisode(episodeId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            _episodeDetails.value = episodeDetailsInteractor.getEpisodeDetails(episodeId)
                .map(mapper::mapEpisodeDetails)
        }
    }

    private suspend fun loadCharactersInEpisode(list: List<String>): Status<List<ListItemUI.CharacterListItemUI>> =
        charactersListInteractor
            .getCharactersInEpisode(list)
            .map(mapper::mapListCharacterInEpisode)

    private suspend fun loadCharacters(charactersIdList: List<String>): List<ListItemUI.CharacterListItemUI> {
        val response = charactersListInteractor.getCharactersInEpisode(charactersIdList)
        return when (response) {
            is Status.Success -> mapper.mapListCharacterInEpisode(response.data)
            is Status.Error -> mapper.mapListCharacterInEpisode(response.data)
            is Status.Initial -> emptyList()
            is Status.EmptyError -> emptyList()
        }
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
    val episode: Status<EpisodeDetailsUI>,
    val characters: Status<List<ListItemUI.CharacterListItemUI>>
)