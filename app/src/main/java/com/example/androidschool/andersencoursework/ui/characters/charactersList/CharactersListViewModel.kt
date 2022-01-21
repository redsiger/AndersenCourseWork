package com.example.androidschool.andersencoursework.ui.characters.charactersList

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.mappers.CharacterUIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersListViewModel (
    private val characterId: Int,
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = CharacterUIMapper()
    private val _charactersList = MutableLiveData<List<CharacterUIEntity>>()
    val charactersList: LiveData<List<CharacterUIEntity>> get() = _charactersList

    init {
//        getCharactersListPaging()
        Log.e("VM", "id: ${characterId.toString()}")
    }

//    private fun getCharactersListPaging() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _charactersList.postValue(
//                charactersInteractor.getCharactersPaging().map {
//                    mapper.fromDomain(it)
//                }
//            )
//        }
//    }

    class Factory @AssistedInject constructor(
        @Assisted("characterId") private val characterId: Int,
        private val charactersInteractor: CharactersInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListViewModel::class.java)
            return CharactersListViewModel(characterId, charactersInteractor) as T
        }

        @AssistedFactory
        interface Factory  {

            fun create(@Assisted("characterId") characterId: Int): CharactersListViewModel.Factory
        }
    }
}