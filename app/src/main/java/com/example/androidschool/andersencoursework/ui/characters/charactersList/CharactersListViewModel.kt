package com.example.androidschool.andersencoursework.ui.characters.charactersList

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = CharacterUIMapper()
    private val _charactersList = MutableLiveData<PagingData<CharacterUIEntity>>()
    val charactersList: LiveData<PagingData<CharacterUIEntity>> get() = _charactersList


    suspend fun getCharactersListPaging() = charactersInteractor.getCharactersPaging().cachedIn(viewModelScope)

    class Factory @AssistedInject constructor(
        private val charactersInteractor: CharactersInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListViewModel::class.java)
            return CharactersListViewModel(charactersInteractor) as T
        }

        @AssistedFactory
        interface Factory  {

            fun create(@Assisted("characterId") characterId: Int): CharactersListViewModel.Factory
        }
    }
}