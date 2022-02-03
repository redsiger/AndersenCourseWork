package com.example.androidschool.andersencoursework.di.viewmodel

import kotlinx.coroutines.CoroutineDispatcher

interface ViewModelDispatcher {

    val coroutineDispatcher: CoroutineDispatcher

    class Base(override val coroutineDispatcher: CoroutineDispatcher): ViewModelDispatcher
}