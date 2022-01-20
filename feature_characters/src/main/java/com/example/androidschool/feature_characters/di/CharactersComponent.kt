package com.example.androidschool.feature_characters.di

import com.example.androidschool.feature_characters.ui.CharactersListFragment
import dagger.Subcomponent

@Subcomponent
interface CharactersComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CharactersComponent
    }

    fun inject(fragment: CharactersListFragment)
}