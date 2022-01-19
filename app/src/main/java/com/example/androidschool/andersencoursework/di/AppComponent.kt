package com.example.androidschool.andersencoursework.di

import androidx.fragment.app.Fragment
import com.example.androidschool.andersencoursework.MainActivity
import com.example.androidschool.core.di.CoreComponent
import com.example.androidschool.core.di.CoreComponentProvider
import com.example.androidschool.core.di.NetworkModule
import com.example.androidschool.feature_characters.di.CharactersComponent
import com.example.androidschool.feature_characters.di.CharactersComponentProvider
import com.example.androidschool.feature_characters.di.CharactersModule
import com.example.androidschool.feature_characters.presenter.CharactersListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    CharactersModule::class,
    SubcomponentsModule::class
])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: CharactersListFragment)

    fun charactersComponent(): CharactersComponent.Factory

    fun coreComponent(): CoreComponent.Factory
}