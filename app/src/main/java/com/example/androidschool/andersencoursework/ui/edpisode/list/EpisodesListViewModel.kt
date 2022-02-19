package com.example.androidschool.andersencoursework.ui.edpisode.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.BasePagingViewModel
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.episode.interactor.EpisodesListInteractor
import com.example.androidschool.domain.episode.model.EpisodeListItem
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class EpisodesListViewModel(
    override val mapToListItemUI: (EpisodeListItem) -> EpisodeListItemUI,
    override val defaultDispatcher: CoroutineDispatcher,
    override val interactor: BasePagingInteractor<EpisodeListItem>
): BasePagingViewModel<EpisodeListItem, EpisodeListItemUI>() {

    override val itemClass: Class<EpisodeListItemUI> = EpisodeListItemUI::class.java

    class Factory @Inject constructor (
        private val interactor: EpisodesListInteractor,
        @DispatcherIO
        private val defaultDispatcher: CoroutineDispatcher,
        private val mapper: UIMapper
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListViewModel::class.java)
            return EpisodesListViewModel(
                mapper::mapEpisodeListItem,
                defaultDispatcher,
                interactor
            ) as T
        }
    }
}