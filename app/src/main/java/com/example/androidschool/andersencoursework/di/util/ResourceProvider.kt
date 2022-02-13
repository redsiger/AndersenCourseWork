package com.example.androidschool.andersencoursework.di.util

import android.content.Context
import javax.inject.Inject

interface ResourceProvider {

    fun getString(resId: Int): String

    class Base @Inject constructor(private val context: Context): ResourceProvider {

        override fun getString(resId: Int) = context.resources.getString(resId)
    }
}