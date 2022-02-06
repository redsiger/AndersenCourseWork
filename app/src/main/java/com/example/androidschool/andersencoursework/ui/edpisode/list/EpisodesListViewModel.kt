package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.androidschool.andersencoursework.di.viewmodel.ViewModelDispatcher
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListViewModel
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val VISIBLE_THRESHOLD = 2

class EpisodesListViewModel(
    private  val interactor: CharactersInteractor,
    private val context: Context,
    private val dispatcher: ViewModelDispatcher
): ViewModel() {

    private val mapper = UIMapper()



    class Factory @Inject constructor(
        private val charactersInteractor: CharactersInteractor,
        private val context: Context,
        private val dispatcher: ViewModelDispatcher
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == EpisodesListViewModel::class.java)
            return EpisodesListViewModel(charactersInteractor, context, dispatcher) as T
        }
    }
}