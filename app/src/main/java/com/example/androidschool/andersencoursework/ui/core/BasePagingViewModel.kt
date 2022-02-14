package com.example.androidschool.andersencoursework.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.util.UIStatePaging
import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.util.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val LIMIT = 10

abstract class BasePagingViewModel<T, R>: ViewModel() {

    abstract val mapToListItemUI: (T) -> ListItem<R>
    abstract val defaultDispatcher: CoroutineDispatcher
    abstract val interactor: BasePagingInteractor<T>

    private val _uiState = MutableStateFlow<UIStatePaging<R>>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging<R>> get() = _uiState.asStateFlow()

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    fun refresh() {
        _uiState.value = UIStatePaging.Refresh(currentData)

        viewModelScope.launch(defaultDispatcher) {
            val response = interactor.getItemsPaging(currentOffset, LIMIT)
            when (response) {
                // check for remote data
                is NetworkResponse.Success -> {
                    val data = response.data.map(mapToListItemUI)
                    // check for end of data
                    if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(
                        data = data,
                        offset = currentOffset
                    )
                    else _uiState.value = UIStatePaging.PartialData(data, currentOffset)
                }
                // check for local data
                is NetworkResponse.Error -> {
                    val data = response.data.map(mapToListItemUI)
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
                val response = interactor.getItemsPaging(currentOffset + LIMIT, LIMIT)
                when (response) {
                    // check for remote data
                    is NetworkResponse.Success -> {
                        val data = response.data.map(mapToListItemUI)
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
}