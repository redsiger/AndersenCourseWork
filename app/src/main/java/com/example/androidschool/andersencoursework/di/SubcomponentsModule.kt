package com.example.androidschool.andersencoursework.di

import com.example.androidschool.core.di.CoreComponent
import com.example.androidschool.feature_characters.di.CharactersComponent
import dagger.Module

@Module(subcomponents = [CoreComponent::class, CharactersComponent::class])
class SubcomponentsModule {}