package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val LIMIT = 10
const val START_OFFSET = 0

class EpisodesListStateViewModel(
    private  val interactor: CharactersInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private val _uiState = MutableStateFlow<UIStatePaging>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging> get() = _uiState.asStateFlow()

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    fun refresh() {
        Log.e("refresh", "start: ${uiState.value}")
        _uiState.value = UIStatePaging.Refresh(currentData)
        viewModelScope.launch(Dispatchers.IO) {
            val response = interactor.getCharactersPagingState(currentOffset, LIMIT)
            when (response) {
                // check for remote data
                is NetworkResponse.Success -> {
                    val data = response.data.map(mapper::mapCharacterEntityToListItem)
                    // check for end of data
                    if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(data, currentOffset)
                    else _uiState.value = UIStatePaging.PartialData(data, currentOffset)
                }
                // check for local data
                is NetworkResponse.Error -> {
                    val data = response.data.map(mapper::mapCharacterEntityToListItem)
                    _uiState.value = UIStatePaging.LoadingPartialDataError(data, currentOffset, response.exception)
                }
            }
        }
        Log.e("refresh", "end: ${uiState.value}")
    }

    fun loadNewPage() {
        Log.e("loadNewPage", "start: ${uiState.value}")

        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value is UIStatePaging.PartialData) {

                Log.e("LoadingNewPage", "offset: $currentOffset")

                _uiState.value = UIStatePaging.LoadingPartialData(currentData + ListItem.Loading, currentOffset)

                Log.e("LoadingNewPage", "offset: $currentOffset")

                val response = interactor.getCharactersPagingState(currentOffset + LIMIT, LIMIT)
                when (response) {
                    // check for remote data
                    is NetworkResponse.Success -> {
                        val data = response.data.map(mapper::mapCharacterEntityToListItem)
                        // check for end of data
                        if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(currentData.filter { it is ListItem.CharacterItem } + data, currentOffset + LIMIT)
                        else _uiState.value = UIStatePaging.PartialData(currentData.filter { it is ListItem.CharacterItem } + data, currentOffset + LIMIT)
                    }
                    // check for local data
                    is NetworkResponse.Error -> {
                        _uiState.value = UIStatePaging.LoadingPartialDataError(currentData.filter { it is ListItem.CharacterItem } + ListItem.Error(response.exception), currentOffset + LIMIT, response.exception)
                    }
                }

            }
        }
        Log.e("loadNewPage", "end: ${uiState.value}")
    }


    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListStateViewModel::class.java)
            return EpisodesListStateViewModel(charactersInteractor, mapper) as T
        }
    }
}

sealed class UIStatePaging {

    abstract val data: List<ListItem>
    abstract val offset: Int
    abstract fun copy(): UIStatePaging

    data class EmptyLoading(
        override val data: List<ListItem> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class EmptyData(
        override val data: List<ListItem> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class EmptyError(
        override val data: List<ListItem>,
        override val offset: Int = START_OFFSET,
        val error: Exception
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class PartialData(
        override val data: List<ListItem>,
        override val offset: Int
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class LoadingPartialData(
        override val data: List<ListItem>,
        override val offset: Int
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class LoadingPartialDataError(
        override val data: List<ListItem>,
        override val offset: Int,
        val error: Exception
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class AllData(
        override val data: List<ListItem>,
        override val offset: Int
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
    data class Refresh(
        override val data: List<ListItem>,
        override val offset: Int = START_OFFSET
    ): UIStatePaging() {
        override fun copy(): UIStatePaging = copy()
    }
}