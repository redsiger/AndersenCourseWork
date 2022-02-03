package com.example.androidschool.util

import java.lang.IllegalStateException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Status<out T> {
    sealed class Success<out T>(val data: T): Status<T>() {
        data class Remote<out T>(val remoteData: T): Status.Success<T>(remoteData)
        data class Local<out T>(val localData: T): Status.Success<T>(localData)
    }
    data class Error(val exception: Throwable): Status<Nothing>()
    object InProgress: Status<Nothing>()

    val extractException: Throwable?
        get() = when (this) {
            is Success -> null
            is Error -> exception
            is InProgress -> null
        }

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
        }

    fun <R> proceed(mapper: Mapper<T, R>? = null) = when(this) {
        is InProgress -> InProgress
        is Error -> Error(this.exception)
        is Success.Local -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success.local status")
            Success.Local(mapper(this.data))
        }
        is Success.Remote -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success.remote status")
            Success.Local(mapper(this.data))
        }
    }
}