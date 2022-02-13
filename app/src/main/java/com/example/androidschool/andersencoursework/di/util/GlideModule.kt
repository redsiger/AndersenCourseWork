package com.example.androidschool.andersencoursework.di.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GlideModule {

    @Singleton
    @Provides
    fun provideGlide(context: Context): RequestManager {
        return Glide.with(context)
    }
}