package com.example.androidschool.andersencoursework.ui.core.recycler

interface DiffComparable {

    val id: Int

    fun areItemsTheSame(other: DiffComparable) = this.id == other.id

    fun areContentsTheSame(other: DiffComparable) = this == other
}
