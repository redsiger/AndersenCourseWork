package com.example.androidschool.util

import java.lang.NullPointerException

sealed class Status<out T> {

//    abstract val data: T

    data class Success<out T>(val data: T): Status<T>()
    data class Error<out T>(val data: T, val exception: Exception): Status<T>()
    object Empty: Status<Nothing>()
    object EmptyError: Status<Nothing>()

    val extractData: T get() =
        when (this) {
            is Success -> data
            is Error -> data
            is Empty -> throw NullPointerException("Status is Empty and doesn't have a data")
            is EmptyError -> throw NullPointerException("Status is EmptyError and doesn't have a data")
        }

    fun <R> map(mapper: (T) -> R) = when(this) {
        is Empty -> Empty
        is EmptyError -> EmptyError
        is Error -> Error(mapper(this.data), this.exception)
        is Success -> Success(mapper(this.data))
    }
}