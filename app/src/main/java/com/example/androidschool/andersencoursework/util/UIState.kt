package com.example.androidschool.andersencoursework.util

sealed class UIState<out T> {
    object InitialLoading: UIState<Nothing>()
    data class Refresh<T>(val currentData: T): UIState<T>()
    data class Success<T>(val data: T): UIState<T>()
    data class Error<T>(val localData: T, val error: Exception): UIState<T>()
}