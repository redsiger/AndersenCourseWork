package com.example.androidschool.domain

import com.example.androidschool.util.NetworkResponse

sealed class DataPiece<out T> {
    data class Remote<out T>(val data: NetworkResponse<T>): DataPiece<T>()
    data class Local<out T>(val data: NetworkResponse<T>): DataPiece<T>()
}