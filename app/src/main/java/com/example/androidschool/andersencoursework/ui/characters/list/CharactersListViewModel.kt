package com.example.androidschool.andersencoursework.ui.characters.list

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import javax.inject.Inject

private val DEFAULT_CONFIG = PagingConfig(pageSize = 10, enablePlaceholders = false)

class CharactersListViewModel constructor (
    private val charactersInteractor: CharactersInteractor,
    private val context: Context,
    private val charactersDao: CharactersDao
): ViewModel() {

//    val charactersFlow: Flow<PagingData<CharacterRoomEntity>>

//    init {
//        charactersFlow = getCharactersListPaging()
//    }

//    private fun getCharactersListPaging(): Flow<PagingData<CharacterRoomEntity>> {
//        val pagingSourceFactory = { charactersDao.getCharactersPagingForPagingSource() }
//
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = DEFAULT_CONFIG,
//            remoteMediator = CharactersRemoteMediator(charactersInteractor, DatabaseMapper()),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
//            .cachedIn(viewModelScope)
//            .flowOn(dispatcher.coroutineDispatcher)
//    }

    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val context: Context,
        private val charactersDao: CharactersDao,
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListViewModel::class.java)
            return CharactersListViewModel(charactersInteractor, context, charactersDao) as T
        }
    }
}