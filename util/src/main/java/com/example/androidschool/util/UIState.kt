package com.example.androidschool.util

interface UIState<T> {

    val TAG: String
    val data: T

    fun refresh() {}
    fun loadNewPage() {}
    fun newData(data: T) {}
    fun fail(error: Exception) {}

}