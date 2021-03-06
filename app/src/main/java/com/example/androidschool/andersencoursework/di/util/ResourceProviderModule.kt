package com.example.androidschool.andersencoursework.di.util

import android.content.Context
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourceProviderModule {

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider
        = ResourceProvider.Base(context)
}