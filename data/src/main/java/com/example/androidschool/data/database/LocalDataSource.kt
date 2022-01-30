package com.example.androidschool.data.database

import com.example.androidschool.data.database.characters.CharactersDao

interface LocalDataSource {

    class Base(
        private val dao: CharactersDao
    ): LocalDataSource {

        fun getCharactersPaging() {

        }
    }
}