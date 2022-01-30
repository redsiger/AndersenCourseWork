package com.example.androidschool.andersencoursework.ui.characters.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharactersListViewModel (
    private val charactersInteractor: CharactersInteractor
): ViewModel() {

    private val mapper = UIMapper()
    private val _charactersList = MutableLiveData<PagingData<CharacterUIEntity>>()
    val charactersList: LiveData<PagingData<CharacterUIEntity>> get() = _charactersList


    suspend fun getCharactersListPaging() = charactersInteractor.getCharactersPaging().cachedIn(viewModelScope)

    suspend fun getCharacter(id: Int): Status<CharacterEntity> {
        val attr = CharacterAttr(id)
        return charactersInteractor.getCharacter(attr)
    }

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