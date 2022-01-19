package com.example.androidschool.andersencoursework.di

import android.app.Application
import com.example.androidschool.feature_characters.di.CharactersComponent
import com.example.androidschool.feature_characters.di.CharactersComponentProvider

class App: Application(), CharactersComponentProvider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun provideCharactersComponent(): CharactersComponent {
        return appComponent.charactersComponent().create()
    }


}