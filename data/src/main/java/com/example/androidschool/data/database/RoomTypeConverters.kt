package com.example.androidschool.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class RoomTypeConverters(private val moshi: Moshi) {

    @TypeConverter
    fun fromStringList(data: List<String>): String {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).toJson(data)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).fromJson(json)!!
    }

    @TypeConverter
    fun fromIntList(data: List<Int>): String {
        val type = Types.newParameterizedType(List::class.java, Integer::class.java)
        return moshi.adapter<List<Int>>(type).toJson(data)
    }

    @TypeConverter
    fun toIntList(json: String): List<Int> {
        val type = Types.newParameterizedType(List::class.java, Integer::class.java)
        return moshi.adapter<List<Int>>(type).fromJson(json)!!
    }
}