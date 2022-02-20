package com.example.androidschool.domain

import com.example.androidschool.util.Status

interface BasePagingInteractor<T> {

     suspend fun getItemsPaging(offset: Int, limit: Int): Status<List<T>>

}