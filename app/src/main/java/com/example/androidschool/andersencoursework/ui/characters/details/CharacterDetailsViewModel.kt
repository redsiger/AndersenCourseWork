package com.example.androidschool.andersencoursework.ui.characters.details

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.util.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_CHARACTER_ID = -1

class CharacterDetailsViewModel @Inject constructor(
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = UIMapper()
    private var _character = MutableLiveData<Status<CharacterUIEntity>>(Status.InProgress)
    val character: LiveData<Status<CharacterUIEntity>> get() = _character
    private var characterId = DEFAULT_CHARACTER_ID

    fun getCharacter(id: Int) {
        characterId = id
        viewModelScope.launch(Dispatchers.IO) {
            val response = charactersInteractor.getCharacter(CharacterAttr(characterId))
            when (response) {
                is Status.Success -> _character.postValue(
                    response.proceed { mapper.mapCharacterEntity(it) })
                is Status.Error -> _character.postValue(response.proceed())
                is Status.InProgress -> _character.postValue(response.proceed())
            }
        }
    }


    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailsViewModel::class.java)
            return CharacterDetailsViewModel(charactersInteractor) as T
        }
    }
}