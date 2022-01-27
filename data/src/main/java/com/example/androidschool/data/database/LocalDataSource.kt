package com.example.androidschool.data.database

interface LocalDataSource {

    class Base(
        private val dao: CharactersDao
    ): LocalDataSource {

        fun getCharactersPaging() {

        }
    }
}