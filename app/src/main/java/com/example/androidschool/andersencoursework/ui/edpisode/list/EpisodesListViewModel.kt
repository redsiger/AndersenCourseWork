package com.example.androidschool.andersencoursework.ui.edpisode.list

import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.util.Status
import com.example.androidschool.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

typealias State = UIState<List<ListItem>>

private const val VISIBLE_THRESHOLD = 2

class EpisodesListViewModel(
    private  val interactor: CharactersInteractor,
): ViewModel() {

    private val mapper = UIMapper()

    private val dispatcher = Dispatchers.IO

    private var _current_offset = 0
    private val limit = 10

    private val _uiState = MutableLiveData<State>(EmptyProgress())
    val uiState: LiveData<State> get() = _uiState

    private var _currentData: MutableList<ListItem> = mutableListOf()

    fun refresh() = _uiState.value?.refresh()
    fun loadNewPage() = _uiState.value?.loadNewPage()

    fun onScroll(
        totalItemCount: Int,
        visibleItemCount: Int,
        lastVisibleItem: Int
    ) {
        val shouldFetchMore = visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount
        if (shouldFetchMore) _uiState.value?.loadNewPage()
    }

    private fun setState(state: State) {
        _uiState.postValue(state)
    }

    private inner class EmptyProgress: State {
        override val TAG: String = "EMPTY_PROGRESS"
        override val data = emptyList<ListItem>()

        override fun newData(data: List<ListItem>) {
            if (data.isEmpty()) {
                setState(EmptyData())
            }
            else {
                _currentData.addAll(data)
                setState(Data())
            }
        }
        override fun fail(error: Exception) = setState(EmptyError(error))

        init {
            viewModelScope.launch(dispatcher) {
                _currentData.clear()
                _current_offset = 0

                val response = interactor.getCharactersPagingState(_current_offset, limit)
                when (response) {
                    is Status.Success -> {
                        val data = response.data

                        newData(
                            data.map { ListItem.CharacterItem(mapper.mapCharacterEntity(it)) }
                        )
                    }
                    is Status.Error -> fail(response.exception)
                }
            }
        }
    }
    
    private inner class Data: State {
        override val TAG: String = "DATA"
        override val data get() = _currentData

        override fun refresh() = setState(Refresh())

        override fun loadNewPage() {
            _current_offset += limit
            setState(PageProgress())
        }
    }

    private inner class Refresh : State {
        override val TAG: String = "REFRESH"
        override val data: List<ListItem> get() = _currentData

        override fun fail(error: Exception) = setState(EmptyError(error))
        override fun newData(data: List<ListItem>) {
            if (data.isEmpty()) {
                _currentData.clear()
                setState(EmptyData())
            }
            else {
                _currentData.clear()
                _currentData.addAll(data)
                setState(Data())
            }
        }

        init {
            viewModelScope.launch(dispatcher) {
                _current_offset = 0

                val response = interactor.getCharactersPagingState(_current_offset, limit)
                when (response) {
                    is Status.Success -> {
                        val data = response.data
                        newData(
                            data.map { ListItem.CharacterItem(mapper.mapCharacterEntity(it)) }
                        )
                    }
                    is Status.Error -> fail(response.exception)
                }
            }
        }
    }

    private inner class EmptyData: State {
        override val TAG: String = "EMPTY_DATA"
        override val data: List<ListItem> get() = _currentData

        override fun refresh() = setState(EmptyProgress())
    }

    private inner class EmptyError(val error: Exception): State {
        override val TAG: String = "EMPTY_ERROR"
        override val data: List<ListItem> get() = _currentData
        init {
            viewModelScope.launch(dispatcher) {
                val localData = interactor
                    .getLocalCharactersPaging(_current_offset, limit)

                _currentData.addAll(
                    localData.map { ListItem.CharacterItem(mapper.mapCharacterEntity(it)) }
                )
            }
        }
        override fun refresh() = setState(EmptyProgress())
    }

    private inner class PageProgress: State {
        override val TAG: String = "PAGE_PROGRESS"
        override val data: List<ListItem> get() = _currentData

        override fun newData(data: List<ListItem>) {
            if (data.size < limit) {
                _currentData.addAll(data)
                setState(AllData())
            } else {
                _currentData.addAll(data)
                setState(Data())
            }
        }
        override fun fail(error: Exception) = setState(PageError(error))

        init {
            viewModelScope.launch(dispatcher) {

                _currentData.add(ListItem.Loading)
                val response = interactor.getCharactersPagingState(_current_offset, limit)
                _currentData.removeLast()

                when (response) {
                    is Status.Success -> {
                        val data = response.data
                        newData(
                            data.map { ListItem.CharacterItem(mapper.mapCharacterEntity(it)) }
                        )
                    }
                    is Status.Error -> fail(response.exception)
                }
            }
        }
    }

    private inner class AllData: State {
        override val TAG: String = "ALL_DATA"
        override val data: List<ListItem> get() = _currentData

        override fun refresh() = setState(Refresh())
    }

    private inner class PageError(private val error: Exception): State {
        override val TAG: String = "PAGE_ERROR"
        override val data: List<ListItem> = _currentData

        override fun refresh() = setState(Refresh())
        override fun fail(error: Exception) = setState(PageError(error))

    }

    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListViewModel::class.java)
            return EpisodesListViewModel(charactersInteractor) as T
        }
    }
}