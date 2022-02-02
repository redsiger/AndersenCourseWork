package com.example.androidschool.andersencoursework.di.viewmodel

import kotlinx.coroutines.CoroutineDispatcher

interface ViewModelDispatcher {

    val dispatcher: CoroutineDispatcher

    class Base(override val dispatcher: CoroutineDispatcher): ViewModelDispatcher
}