package com.example.androidschool.andersencoursework.di

import android.content.Context
import com.example.androidschool.andersencoursework.di.dispatchers.CoroutineDispatchersModule
import com.example.androidschool.andersencoursework.di.util.GlideModule
import com.example.androidschool.andersencoursework.di.util.ResourceProviderModule
import com.example.androidschool.andersencoursework.di.util.UIMapperModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        CoroutineDispatchersModule::class,
        ResourceProviderModule::class,
        GlideModule::class,
        UIMapperModule::class
    ]
)
class AppModule(val context: Context) {

    @Singleton
    @Provides
    fun provideAppContext(): Context {
        return context
    }
}