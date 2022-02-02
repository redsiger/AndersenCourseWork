package com.example.androidschool.andersencoursework.di

import android.app.Application
import android.content.Context
import com.example.androidschool.data.di.DataComponent
import com.example.androidschool.data.di.DataComponentProvider

class App: Application(), DataComponentProvider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun provideDataComponent(): DataComponent {
        return appComponent.dataComponent().create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }