package com.example.androidschool.andersencoursework.ui.characters.details

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.domain.characters.interactor.CharacterDetailsInteractor
import com.example.androidschool.domain.seasons.interactor.SeasonsInteractor
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsViewModel constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val characterInteractor: CharacterDetailsInteractor,
    private val seasonsInteractor: SeasonsInteractor,
    private val mapper: UIMapper
) : ViewModel() {

    private val _character = MutableStateFlow<Status<CharacterDetailsUI>>(Status.Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _appearance: Flow<Status<List<SeasonListItemUI>>> =
        _character.flatMapLatest { character ->
            flowOf(
                when (character) {
                    is Status.Initial -> Status.Initial
                    is Status.EmptyError -> Status.EmptyError
                    else -> loadAppearance(character.extractData.appearance.map { it.toString() })
                }
            )
                .flowOn(defaultDispatcher)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(0),
                    initialValue = Status.Initial
                )
        }

    val uiState: Flow<CharacterDetailsState> =
        combine(_character, _appearance, ::makeState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(0),
                initialValue = CharacterDetailsState(
                    character = Status.Initial,
                    appearance = Status.Initial
                )
            )

    fun load(characterId: Int) {
        loadCharacter(characterId)
    }

    fun retry() {
        _character.value = Status.Initial
    }

    private fun loadCharacter(characterId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            _character.value = characterInteractor.getCharacterDetails(characterId)
                .map(mapper::mapCharacterDetails)
        }
    }

    private suspend fun loadAppearance(list: List<String>): Status<List<SeasonListItemUI>> =
        seasonsInteractor
            .getSeasonsByAppearance(list)
            .map(mapper::mapListSeasonListItem)

    private fun makeState(
        character: Status<CharacterDetailsUI>,
        appearance: Status<List<SeasonListItemUI>>
    ): CharacterDetailsState = CharacterDetailsState(character, appearance)

    class Factory @Inject constructor(
        @DispatcherIO
        private val defaultDispatcher: CoroutineDispatcher,
        private val characterInteractor: CharacterDetailsInteractor,
        private val seasonsInteractor: SeasonsInteractor,
        private val mapper: UIMapper
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(
                defaultDispatcher,
                characterInteractor,
                seasonsInteractor,
                mapper
            ) as T
        }
    }
}

data class CharacterDetailsState(
    val character: Status<CharacterDetailsUI>,
    val appearance: Status<List<SeasonListItemUI>>
)