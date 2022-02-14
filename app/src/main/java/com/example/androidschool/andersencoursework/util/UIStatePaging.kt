package com.example.androidschool.andersencoursework.util

import com.example.androidschool.andersencoursework.ui.characters.models.ListItem

const val START_OFFSET = 0

sealed class UIStatePaging<T> {

    abstract val data: List<ListItem<T>>
    abstract val offset: Int
    abstract fun copy(): UIStatePaging<T>

    data class EmptyLoading<T>(
        override val data: List<ListItem<T>> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class EmptyData<T>(
        override val data: List<ListItem<T>> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class EmptyError<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int = START_OFFSET,
        val error: Exception
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class PartialData<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class LoadingPartialData<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class LoadingPartialDataError<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int,
        val error: Exception
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class AllData<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class Refresh<T>(
        override val data: List<ListItem<T>>,
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
}