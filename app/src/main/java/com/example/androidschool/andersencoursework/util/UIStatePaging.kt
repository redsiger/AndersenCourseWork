package com.example.androidschool.andersencoursework.util

import com.example.androidschool.andersencoursework.ui.core.recycler.ListItem
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable

const val START_OFFSET = 0

sealed class UIStatePaging<T: DiffComparable> {

    abstract val data: List<T>
    abstract val offset: Int
    abstract fun copy(): UIStatePaging<T>

    data class EmptyLoading<T: DiffComparable>(
        override val data: List<T> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class EmptyData<T: DiffComparable>(
        override val data: List<T> = emptyList(),
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class EmptyError<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int = START_OFFSET,
        val error: Exception
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class PartialData<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class LoadingPartialData<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class LoadingPartialDataError<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int,
        val error: Exception
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class AllData<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
    data class Refresh<T: DiffComparable>(
        override val data: List<T>,
        override val offset: Int = START_OFFSET
    ): UIStatePaging<T>() {
        override fun copy(): UIStatePaging<T> = copy()
    }
}