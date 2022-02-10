package com.example.androidschool.andersencoursework.ui.characters.list

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.util.UIStatePaging
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LIMIT = 10
const val START_OFFSET = 0

class CharactersListStateViewModel(
    private  val interactor: CharactersInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private val _uiState = MutableStateFlow<UIStatePaging<CharacterUIEntity>>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging<CharacterUIEntity>> get() = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            val state = uiState.collectLatest {
                Log.e("CURRENT STATE", "$it")
            }
        }
    }

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    fun refresh() {
        _uiState.value = UIStatePaging.Refresh(currentData)
        viewModelScope.launch(Dispatchers.IO) {
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
        Log.e("loadNewPage", "start: ${uiState.value}")

        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value is UIStatePaging.PartialData || _uiState.value is UIStatePaging.LoadingPartialDataError) {
                _uiState.value = UIStatePaging.LoadingPartialData(
                    data = currentData.filter { it is ListItem.Item } + ListItem.Loading(),
                    offset = currentOffset
                )
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
        private val charactersInteractor: CharactersInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListStateViewModel::class.java)
            return CharactersListStateViewModel(charactersInteractor, mapper) as T
        }
    }
}