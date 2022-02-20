package com.example.androidschool.andersencoursework.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.andersencoursework.ui.core.recycler.DefaultRecyclerError
import com.example.androidschool.andersencoursework.ui.core.recycler.DefaultRecyclerLoading
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.util.UIStatePaging
import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.util.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val LIMIT = 10

abstract class BasePagingViewModel<T, R: DiffComparable>: ViewModel() {

    abstract val itemClass: Class<R>
    abstract val mapToListItemUI: (T) -> R
    abstract val defaultDispatcher: CoroutineDispatcher
    abstract val interactor: BasePagingInteractor<T>

    private val _uiState = MutableStateFlow<UIStatePaging<DiffComparable>>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging<DiffComparable>> get() = _uiState.asStateFlow()

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    private fun isItem(item: DiffComparable) = item::class.java == itemClass

    fun refresh() {
        _uiState.value = UIStatePaging.Refresh(currentData)

        viewModelScope.launch(defaultDispatcher) {
            val response = interactor.getItemsPaging(currentOffset, LIMIT)
            when (response) {
                // check for remote data
                is Status.Success -> {
                    val data = response.data.map(mapToListItemUI)
                    // check for end of data
                    if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(
                        data = data,
                        offset = currentOffset
                    )
                    else _uiState.value = UIStatePaging.PartialData(data, currentOffset)
                }
                // check for local data
                is Status.Error -> {
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
                data = currentData.filter { isItem(it) } + DefaultRecyclerLoading(),
                offset = currentOffset
            )

            viewModelScope.launch(defaultDispatcher) {
                val response = interactor.getItemsPaging(currentOffset + LIMIT, LIMIT)
                when (response) {
                    // check for remote data
                    is Status.Success -> {
                        val data = response.data.map(mapToListItemUI)
                        // check for end of data
                        if (data.size < LIMIT) _uiState.value = UIStatePaging.AllData(
                            data = currentData.filter { isItem(it) } + data,
                            offset = currentOffset + LIMIT
                        )
                        else _uiState.value = UIStatePaging.PartialData(currentData
                            .filter { isItem(it) } + data,
                            currentOffset + LIMIT)
                    }
                    // check for local data
                    is Status.Error -> {
                        val error = response.exception
                        _uiState.value = UIStatePaging.LoadingPartialDataError(
                            data = currentData.filter { isItem(it) } + DefaultRecyclerError(error = error),
                            offset = currentOffset,
                            error = error
                        )
                    }
                }

            }
        }
    }
}