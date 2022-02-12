package com.example.androidschool.andersencoursework.ui.characters.details

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.domain.characters.interactors.CharacterDetailsInteractor
import com.example.androidschool.util.NetworkResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
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
        viewModelScope.launch(Dispatchers.IO) {
            val response = interactor.getCharacterDetails(characterId)
            when (response) {
                is NetworkResponse.Success -> {
                    _character.value = UIState.Success(
                        data = mapper.mapCharacterDetailEntity(response.data)
                    )
                }
                is NetworkResponse.Error -> {
                    _character.value = UIState.Error(
                        mapper.mapCharacterDetailEntity(response.data),
                        response.exception
                    )
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val interactor: CharacterDetailsInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(interactor, mapper) as T
        }
    }
}