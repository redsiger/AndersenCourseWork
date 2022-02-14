package com.example.androidschool.domain

import com.example.androidschool.util.NetworkResponse

interface BasePagingInteractor<T> {

     suspend fun getItemsPaging(offset: Int, limit: Int): NetworkResponse<List<T>>
}