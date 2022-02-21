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

// something terrible
fun <T> Status<List<T>>.merge(
    other: Status<List<T>>,
    mergeFunction: (List<T>, List<T>) -> List<T>
): Status<List<T>> {
    return when (this.areSameAndNotEmpty(other)) {
        true -> {
            val firstList = this.extractData
            val secondList = other.extractData
            when (this) {
                is Status.Success -> Status.Success(mergeFunction(firstList, secondList))
                is Status.Error -> Status.Error(
                    mergeFunction(firstList, secondList),
                    this.exception
                )
                else -> throw IllegalStateException("Status is incorrect")
            }
        }
        false -> when (this) {
            is Status.Success -> Status.Success(this.extractData)
            is Status.Error -> Status.Error(this.extractData, this.exception)
            else -> when (other) {
                is Status.Success -> Status.Success(other.extractData)
                is Status.Error -> Status.Error(other.extractData, other.exception)
                else -> throw IllegalStateException("Status: neither one nor the other")
            }
        }
    }
}