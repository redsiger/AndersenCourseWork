package com.example.androidschool.andersencoursework.ui.characters.characterDetail

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.mappers.CharacterUIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.util.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val characterId: Int,
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = CharacterUIMapper()
    private var _character = MutableLiveData<Status<CharacterUIEntity>>(Status.InProgress)
    val character: LiveData<Status<CharacterUIEntity>> get() = _character

    init {
        getCharacter()
        Log.e("DETAILS", this.toString())
    }

    private fun getCharacter() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = charactersInteractor.getCharacter(CharacterAttr(characterId))
            when (response) {
                is Status.Success -> _character.postValue(
                    response.proceed { mapper.fromDomain(it.first()) })
                is Status.Error -> _character.postValue(response.proceed())
                is Status.InProgress -> _character.postValue(response.proceed())
            }
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("characterId") private val characterId: Int,
        private val charactersInteractor: CharactersInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(characterId, charactersInteractor) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted("characterId") characterId: Int): CharacterDetailsViewModel.Factory
        }
    }
}