package com.example.androidschool.core.di

import dagger.Subcomponent

@Subcomponent
interface CoreComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CoreComponent
    }
}