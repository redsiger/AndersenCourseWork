package com.example.androidschool.andersencoursework.ui.characters.details

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

private const val DEFAULT_CHARACTER_ID = -1

class CharacterDetailsViewModel @Inject constructor(
    private val charactersInteractor: CharactersInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private var _character = MutableStateFlow<UIState<CharacterUIEntity>>(UIState.InitialLoading)
    val character: StateFlow<UIState<CharacterUIEntity>> get() = _character
    private var characterId = DEFAULT_CHARACTER_ID



    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(charactersInteractor, mapper) as T
        }
    }
}