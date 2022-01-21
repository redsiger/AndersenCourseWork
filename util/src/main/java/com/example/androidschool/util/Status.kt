package com.example.androidschool.util

import java.lang.IllegalStateException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Status<out T> {
    data class Success<out T>(val data: T): Status<T>()
    data class Error(val exception: Exception): Status<Nothing>()
    object InProgress: Status<Nothing>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
        }

    fun <R> proceed(mapper: Mapper<T, R>? = null) = when(this) {
        is InProgress -> InProgress
        is Error -> Error(this.exception)
        is Success -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success status")
            Success(mapper(this.data))
        }
    }
}