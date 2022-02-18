package com.example.androidschool.andersencoursework.util

sealed class UIState<out T> {
    
    object InitialLoading: UIState<Nothing>()
    object Refresh: UIState<Nothing>()
    data class Success<T>(val data: T): UIState<T>()
    data class Error<T>(val data: T, val error: Exception): UIState<T>()
    data class EmptyError(val error: Exception): UIState<Nothing>()
}