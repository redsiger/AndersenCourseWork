package com.example.androidschool.andersencoursework.ui.edpisode.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListStateViewModel
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.util.UIStatePaging
import com.example.androidschool.domain.episode.interactors.EpisodesListInteractor
import com.example.androidschool.domain.episode.model.EpisodeListItem
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val LIMIT = 10
const val START_OFFSET = 0

class EpisodesListViewModel(
    private val interactor: EpisodesListInteractor,
    private val mapper: UIMapper
): ViewModel() {

    private val _uiState = MutableStateFlow<UIStatePaging<EpisodeListItemUI>>(UIStatePaging.EmptyLoading())
    val uiState: StateFlow<UIStatePaging<EpisodeListItemUI>> get() = _uiState.asStateFlow()

    private val currentOffset get() = uiState.value.offset
    private val currentData get() = uiState.value.data

    class Factory @Inject constructor (
        private val interactor: EpisodesListInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListViewModel::class.java)
            return EpisodesListViewModel(interactor, mapper) as T
        }
    }
}