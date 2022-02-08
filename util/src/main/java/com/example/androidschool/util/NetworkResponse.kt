package com.example.androidschool.util

sealed class NetworkResponse<out T> {

    abstract val data: T

    data class Success<out T>(override val data: T): NetworkResponse<T>()
    data class Error<out T>(override val data: T, val exception: Exception): NetworkResponse<T>()

}