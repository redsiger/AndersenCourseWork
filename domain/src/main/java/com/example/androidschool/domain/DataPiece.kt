package com.example.androidschool.domain

import com.example.androidschool.util.Status

sealed class DataPiece<out T> {
    data class Remote<out T>(val data: Status<T>): DataPiece<T>()
    data class Local<out T>(val data: Status<T>): DataPiece<T>()
}