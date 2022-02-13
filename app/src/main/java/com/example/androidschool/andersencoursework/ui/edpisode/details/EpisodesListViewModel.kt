package com.example.androidschool.andersencoursework.ui.edpisode.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListStateViewModel
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.episode.interactors.EpisodesListInteractor
import javax.inject.Inject

class EpisodesListViewModel(
    private val interactor: EpisodesListInteractor,
    private val mapper: UIMapper
): ViewModel() {

    class Factory @Inject constructor (
        private val interactor: EpisodesListInteractor,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListViewModel::class.java)
            return EpisodesListViewModel(interactor, mapper) as T
        }
    }
}