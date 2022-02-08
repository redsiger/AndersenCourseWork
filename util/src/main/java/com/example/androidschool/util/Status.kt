package com.example.androidschool.util

import java.lang.IllegalStateException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Status<out T> {
    data class Success<out T>(val data: T): Status<T>()
    data class Error(val exception: Exception): Status<Nothing>()

    val extractException: Throwable?
        get() = when (this) {
            is Success -> null
            is Error -> exception
        }

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
        }

    fun <R> proceed(mapper: Mapper<T, R>? = null) = when(this) {
        is Error -> Error(this.exception)
        is Success -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success.local status")
            Success(mapper(this.data))
        }
    }
}