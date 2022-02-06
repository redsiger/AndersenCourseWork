package com.example.androidschool.util

import java.lang.Exception

sealed class PagingStatus<out T> {
    data class Remote<out T>(val data: T): PagingStatus<T>()
    data class Local<out T>(
        val data: T,
        val exception: Exception
        ): PagingStatus<T>()
}