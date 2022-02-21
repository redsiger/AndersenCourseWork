package com.example.androidschool.util

import java.lang.NullPointerException

sealed class Status<out T> {

    data class Success<out T>(val data: T) : Status<T>()
    data class Error<out T>(val data: T, val exception: Exception) : Status<T>()
    object Initial : Status<Nothing>()
    object EmptyError : Status<Nothing>()

    val extractData: T
        get() =
            when (this) {
                is Success -> data
                is Error -> data
                is Initial -> throw NullPointerException("Status is Empty and doesn't have a data")
                is EmptyError -> throw NullPointerException("Status is EmptyError and doesn't have a data")
            }

    fun <R> map(mapper: (T) -> R) = when (this) {
        is Initial -> Initial
        is EmptyError -> EmptyError
        is Error -> Error(mapper(this.data), this.exception)
        is Success -> Success(mapper(this.data))
    }

    fun <T> areSameAndNotEmpty(other: Status<T>): Boolean {
        return when {
            this is Success && other is Success -> true
            this is Error && other is Error -> true
            else -> false
        }
    }
}

fun <T> Status<List<T>>.filter(
    filter: (T) -> Boolean
): Status<List<T>> {
    return when (this) {
        is Status.Success -> Status.Success(this.data.filter(filter))
        is Status.Error -> Status.Error(this.data.filter(filter), this.exception)
        is Status.EmptyError -> Status.EmptyError
        is Status.Initial -> Status.Initial
    }
}

// something terrible
fun <T> Status<List<List<T>>>.flatten(): Status<List<T>> {
    return when (this) {
        is Status.Initial -> Status.Initial
        is Status.EmptyError -> Status.EmptyError
        is Status.Success -> Status.Success(this.data.flatten())
        is Status.Error -> Status.Error(this.data.flatten(), this.exception)
    }
}

// something terrible
fun <T> Status<List<T>>.merge(
    other: Status<List<T>>,
): Status<List<List<T>>> {
    return when (this.areSameAndNotEmpty(other)) {
        true -> {
            val firstList = this.extractData
            val secondList = other.extractData
            when (this) {
                is Status.Success -> Status.Success(listOf(firstList, secondList))
                is Status.Error -> Status.Error(
                    listOf(firstList, secondList),
                    this.exception
                )
                else -> throw IllegalStateException("Status is incorrect")
            }
        }
        false -> when (this) {
            is Status.Success -> Status.Success(listOf(this.extractData))
            is Status.Error -> Status.Error(listOf(this.extractData), this.exception)
            else -> when (other) {
                is Status.Success -> Status.Success(listOf(other.extractData))
                is Status.Error -> Status.Error(listOf(other.extractData), other.exception)
                else -> throw IllegalStateException("Status: neither one nor the other")
            }
        }
    }
}