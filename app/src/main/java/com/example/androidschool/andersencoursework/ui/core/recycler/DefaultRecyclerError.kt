package com.example.androidschool.andersencoursework.ui.core.recycler

const val RECYCLER_ERROR = -1

data class DefaultRecyclerError(
    override val id: Int = RECYCLER_ERROR,
    val error: Exception
): DiffComparable