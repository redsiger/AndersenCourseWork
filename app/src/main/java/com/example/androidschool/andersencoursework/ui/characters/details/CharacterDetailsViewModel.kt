package com.example.androidschool.andersencoursework.ui.characters.details

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactors.CharacterDetailsInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class CharacterDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val interactor: CharacterDetailsInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private var _character = MutableStateFlow<UIState<CharacterDetailsUI>>(UIState.InitialLoading)
    val character: StateFlow<UIState<CharacterDetailsUI>> get() = _character.asStateFlow()

    init {
        viewModelScope.launch {
            character.collectLatest {
                Log.e("CharacterDetailsViewModel", "state:$it")
            }
        }
    }

    fun loadCharacter(characterId: Int) {
        _character.value = UIState.InitialLoading
        load(characterId)
    }

    fun refresh(data: CharacterDetailsUI, characterId: Int) {
        _character.value = UIState.Refresh(data)
        load(characterId)
    }

    fun retry(characterId: Int) {
        _character.value = UIState.InitialLoading
        load(characterId)
    }

    private fun load(characterId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            val response = interactor.getCharacterDetails(characterId)
            when (response) {
                is NetworkResponse.Success -> {
                    _character.value = UIState.Success(
                        data = mapper.mapCharacterDetailEntity(response.data)
                    )
                }
                is NetworkResponse.Error -> {
                    when (response.data.charId) {
                        -1 -> _character.value = UIState.EmptyError(response.exception)
                        else -> _character.value = UIState.Error(
                            data = mapper.mapCharacterDetailEntity(response.data),
                            error = response.exception
                        )
                    }
                }
            }
        }
    }

    class Factory @Inject constructor(
        @Named("Dispatchers.IO") private val defaultDispatcher: CoroutineDispatcher,
        private val interactor: CharacterDetailsInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(defaultDispatcher, interactor, mapper) as T
        }
    }
}