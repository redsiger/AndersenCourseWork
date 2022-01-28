package com.example.androidschool.data.di

import android.content.Context
import com.example.androidschool.data.database.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context, typeConverters: RoomTypeConverters): AppDatabase {
        return AppDatabase.getInstance(context, typeConverters)
    }

    @Provides
    fun provideRoomTypeConverters(moshi: Moshi): RoomTypeConverters {
        return RoomTypeConverters(moshi)
    }

    @Singleton
    @Provides
    fun provideCharactersDao(database: AppDatabase): CharactersDao {
        return database.getCharactersDao()
    }

    @Provides
    fun provideCharactersStorage(dao: CharactersDao, mapper: DatabaseMapper): CharactersStorage {
        return  CharactersStorage(dao, mapper)
    }

    @Provides
    fun provideDatabaseMapper(): DatabaseMapper = DatabaseMapper()
}