package com.example.androidschool.andersencoursework.ui.characters.list

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.*
import com.example.androidschool.andersencoursework.di.viewmodel.ViewModelDispatcher
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.mediators.CharactersRemoteMediator
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private val DEFAULT_CONFIG = PagingConfig(pageSize = 10, enablePlaceholders = false)

class CharactersListViewModel constructor (
    private val charactersInteractor: CharactersInteractor,
    private val context: Context,
    private val charactersDao: CharactersDao,
    private val dispatcher: ViewModelDispatcher
): ViewModel() {

    private val mapper = UIMapper()

    val charactersFlow: Flow<PagingData<CharacterRoomEntity>>

    init {
        charactersFlow = getCharactersListPaging()
    }

    private fun getCharactersListPaging(): Flow<PagingData<CharacterRoomEntity>> {
        val pagingSourceFactory = { charactersDao.getCharactersPagingForPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = DEFAULT_CONFIG,
            remoteMediator = CharactersRemoteMediator(charactersInteractor, DatabaseMapper()),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .flowOn(dispatcher.coroutineDispatcher)
    }

    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val context: Context,
        private val charactersDao: CharactersDao,
        private val dispatcher: ViewModelDispatcher
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListViewModel::class.java)
            return CharactersListViewModel(charactersInteractor, context, charactersDao, dispatcher) as T
        }
    }
}