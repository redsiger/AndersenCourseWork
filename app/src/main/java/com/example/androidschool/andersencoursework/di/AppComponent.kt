package com.example.androidschool.andersencoursework.di

import com.example.androidschool.andersencoursework.di.domain.DomainModule
import com.example.androidschool.andersencoursework.ui.characters.details.CharacterDetailsFragment
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListFragment
import com.example.androidschool.andersencoursework.ui.edpisode.details.EpisodeDetailsFragment
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListFragment
import com.example.androidschool.andersencoursework.ui.map.MapFragment
import com.example.androidschool.andersencoursework.ui.search.SearchFragment
import com.example.androidschool.andersencoursework.ui.seasons.details.SeasonDetailsFragment
import com.example.androidschool.andersencoursework.ui.seasons.list.SeasonsListFragment
import com.example.androidschool.data.di.DataComponent
import com.example.androidschool.data.di.DatabaseModule
import com.example.androidschool.data.di.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    DomainModule::class,
    SubcomponentsModule::class
])
interface AppComponent {

    fun dataComponent(): DataComponent.Factory

    fun inject(fragment: CharacterDetailsFragment)
    fun inject(fragment: CharactersListFragment)
    fun inject(fragment: SeasonsListFragment)
    fun inject(fragment: SeasonDetailsFragment)
    fun inject(fragment: EpisodesListFragment)
    fun inject(fragment: EpisodeDetailsFragment)
    fun inject(fragment: SearchFragment)

    fun inject(fragment: MapFragment)
}