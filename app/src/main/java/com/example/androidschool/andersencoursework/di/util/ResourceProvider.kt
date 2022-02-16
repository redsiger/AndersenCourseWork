package com.example.androidschool.andersencoursework.di.util

import android.content.Context
import android.content.res.Resources
import javax.inject.Inject

interface ResourceProvider {

    val resources: Resources

    class Base @Inject constructor(private val context: Context): ResourceProvider {
        override val resources: Resources = context.resources
    }
}