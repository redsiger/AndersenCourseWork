package com.example.androidschool.domain.search.model

const val TYPE_CHARACTER = "CHARACTER"
const val TYPE_EPISODE = "EPISODE"

interface SearchItem {
    val type: String
}