package com.example.androidschool.andersencoursework.ui.characters.list

import android.util.Log
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.di.dispatchers.DispatcherIO
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.BasePagingViewModel
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.domain.BasePagingInteractor
import com.example.androidschool.domain.characters.interactor.CharactersListInteractor
import com.example.androidschool.domain.search.model.ListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersListStateViewModel(
    override val mapToListItemUI: (ListItem.CharacterListItem) -> ListItemUI.CharacterListItemUI,
    override val defaultDispatcher: CoroutineDispatcher,
    override val interactor: BasePagingInteractor<ListItem.CharacterListItem>
) : BasePagingViewModel<ListItem.CharacterListItem, ListItemUI.CharacterListItemUI>() {

    override val itemClass: Class<ListItemUI.CharacterListItemUI> =
        ListItemUI.CharacterListItemUI::class.java

    init {
        viewModelScope.launch {
            uiState.collectLatest {
                Log.e("CharactersListStateViewModel", "state:$it")
            }
        }
    }


    class Factory @Inject constructor(
        private val charactersListInteractor: CharactersListInteractor,
        private val mapper: UIMapper,
        @DispatcherIO
        private val defaultDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersListStateViewModel::class.java)
            return CharactersListStateViewModel(
                mapper::mapCharacterListItem,
                defaultDispatcher,
                charactersListInteractor
            ) as T
        }
    }
}