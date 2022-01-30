package com.example.androidschool.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity

@Database(entities = [CharacterRoomEntity::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    companion object {

        @Volatile
        private var database: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context, typeConverters: RoomTypeConverters): AppDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "AppDatabase"
                )
                    .addTypeConverter(typeConverters)
                    .build()
                database as AppDatabase
            } else database as AppDatabase
        }
    }

    abstract fun getCharactersDao(): CharactersDao

}