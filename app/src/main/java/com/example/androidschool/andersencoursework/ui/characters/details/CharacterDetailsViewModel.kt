package com.example.androidschool.andersencoursework.ui.characters.details

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_CHARACTER_ID = -1

class CharacterDetailsViewModel @Inject constructor(
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = UIMapper()
    private var _character = MutableLiveData<NetworkResponse<CharacterUIEntity?>>()
    val character: LiveData<NetworkResponse<CharacterUIEntity?>> get() = _character
    private var characterId = DEFAULT_CHARACTER_ID

//    fun getCharacter(id: Int) {
//        characterId = id
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = charactersInteractor.getCharacter(characterId)
//
//            _character.postValue(mapper.mapCharacterEntity(response.data))
//        }
//    }


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