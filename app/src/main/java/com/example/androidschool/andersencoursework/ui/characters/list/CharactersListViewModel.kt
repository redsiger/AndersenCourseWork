package com.example.androidschool.andersencoursework.ui.characters.list

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidschool.andersencoursework.di.viewmodel.ViewModelDispatcher
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.data.repositories.characters.LoadCharactersAction
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.PagingAttr
import com.example.androidschool.util.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val DEFAULT_CONFIG = PagingConfig(pageSize = 10, enablePlaceholders = false)

class CharactersListViewModel @Inject constructor (
    private val charactersInteractor: CharactersInteractor,
    private val dispatcher: ViewModelDispatcher
): ViewModel() {

    val charactersFlow: Flow<PagingData<CharacterUIEntity>>

    init {
        charactersFlow = getCharactersListPaging()
    }

    private fun getCharactersListPaging(): Flow<PagingData<CharacterUIEntity>> {
        val loadAction : LoadCharactersAction = { limit, offset ->
            charactersInteractor.getCharactersPaging(PagingAttr(limit, offset))
        }

        return Pager(
            config = DEFAULT_CONFIG,
            pagingSourceFactory = { CharactersPagingSource(loadAction, UIMapper()) }
        ).flow
    }

    suspend fun getCharacter(id: Int): Status<CharacterEntity> {
        val attr = CharacterAttr(id)
        return charactersInteractor.getCharacter(attr)
    }

    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val dispatcher: ViewModelDispatcher
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListViewModel::class.java)
            return CharactersListViewModel(charactersInteractor, dispatcher) as T
        }
    }
}