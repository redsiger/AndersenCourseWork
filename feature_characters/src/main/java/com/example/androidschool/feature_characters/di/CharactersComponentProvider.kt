package com.example.androidschool.feature_characters.di

import dagger.Provides

interface CharactersComponentProvider {
    fun provideCharactersComponent(): CharactersComponent
}