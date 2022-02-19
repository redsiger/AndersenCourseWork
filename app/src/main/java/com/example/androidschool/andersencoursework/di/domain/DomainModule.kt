package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.andersencoursework.di.domain.CharactersModule
import com.example.androidschool.andersencoursework.di.domain.EpisodesModule
import dagger.Module

@Module(
    includes = [
        CharactersModule::class,
        EpisodesModule::class,
        SeasonsModule::class
    ]
)
class DomainModule