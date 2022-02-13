package com.example.androidschool.andersencoursework.ui.characters.list

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.util.UIStatePaging
import com.example.androidschool.domain.characters.interactors.CharactersListInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val LIMIT = 10
const val START_OFFSET = 0

class CharactersListStateViewModel(
    private  val interactor: CharactersListInteractor,
    private val mapper: UIMapper,
    private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UIStatePaging<CharacterListItemUI>>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging<CharacterListItemUI>> get() = _uiState.asStateFlow()

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    init {
        viewModelScope.launch {
            uiState.collectLatest{
                Log.e("CharactersListStateViewModel", "state:$it")
            }
        }
    }

    fun refresh() {
        _uiState.value = UIStatePaging.Refresh(currentData)

        viewModelScope.launch(defaultDispatcher) {
            val response = interactor.getCharactersPagingState(currentOffset, LIMIT)
            when (response) {
                // check for remote data
                is NetworkResponse.Success -> {
                    val data = response.data.map(mapper::mapCharacterEntityToListItem)
                    // check for end of data
                    if (data.size < LIMIT) _uiState.value =
                        UIStatePaging.AllData(data, currentOffset)
                    else _uiState.value = UIStatePaging.PartialData(data, currentOffset)
                }
                // check for local data
                is NetworkResponse.Error -> {
                    val data = response.data.map(mapper::mapCharacterEntityToListItem)
                    if (data.isEmpty()) _uiState.value = UIStatePaging.EmptyError(currentData, currentOffset, response.exception)
                    else _uiState.value = UIStatePaging.LoadingPartialDataError(
                        data,
                        currentOffset,
                        response.exception
                    )
                }
            }
        }
    }

    fun loadNewPage() {
        if (_uiState.value is UIStatePaging.PartialData || _uiState.value is UIStatePaging.LoadingPartialDataError) {

            _uiState.value = UIStatePaging.LoadingPartialData(
                data = currentData.filter { it is ListItem.Item } + ListItem.Loading(),
                offset = currentOffset
            )

            viewModelScope.launch(defaultDispatcher) {
                val response = interactor.getCharactersPagingState(currentOffset + LIMIT, LIMIT)
                when (response) {
                    // check for remote data
                    is NetworkResponse.Success -> {
                        val data = response.data.map(mapper::mapCharacterEntityToListItem)
                        // check for end of data
                        if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(
                            data = currentData.filter { it is ListItem.Item } + data,
                            offset = currentOffset + LIMIT
                        )
                        else _uiState.value = UIStatePaging.PartialData(currentData
                            .filter { it is ListItem.Item } + data,
                            currentOffset + LIMIT)
                    }
                    // check for local data
                    is NetworkResponse.Error -> {
                        val error = response.exception
                        _uiState.value = UIStatePaging.LoadingPartialDataError(
                            data = currentData.filter { it is ListItem.Item } + ListItem.Error(error),
                            offset = currentOffset,
                            error = error
                        )
                    }
                }

            }
        }
    }

    class Factory @Inject constructor(
        private val charactersListInteractor: CharactersListInteractor,
        private val mapper: UIMapper,
        @Named("Dispatchers.IO") private val defaultDispatcher: CoroutineDispatcher
    ): ViewModelProvider.Factory {
        
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListStateViewModel::class.java)
            return CharactersListStateViewModel(charactersListInteractor, mapper, defaultDispatcher) as T
        }
    }
}