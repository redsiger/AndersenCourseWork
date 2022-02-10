package com.example.androidschool.andersencoursework.di

import com.example.androidschool.andersencoursework.ui.characters.details.CharacterDetailsFragment
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListFragment
import com.example.androidschool.andersencoursework.ui.favorites.FavoritesFragment
import com.example.androidschool.andersencoursework.ui.seacrh.SearchFragment
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
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SearchFragment)
}