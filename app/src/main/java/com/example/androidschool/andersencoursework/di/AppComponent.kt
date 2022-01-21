package com.example.androidschool.andersencoursework.di

import android.content.Context
import com.example.androidschool.andersencoursework.ui.MainActivity
import com.example.androidschool.andersencoursework.ui.characters.characterDetail.CharacterDetailsFragment
import com.example.androidschool.andersencoursework.ui.characters.charactersList.CharactersListFragment
import com.example.androidschool.data.di.DataComponent
import com.example.androidschool.data.di.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DomainModule::class,
    SubcomponentsModule::class
])
interface AppComponent {

    fun dataComponent(): DataComponent.Factory

    fun inject(activity: MainActivity)
    fun inject(fragment: CharactersListFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }