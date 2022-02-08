package com.example.androidschool.data.di

import android.content.Context
import com.example.androidschool.data.database.*
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.repositories.characters.CharactersLocalStorage
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
    fun provideCharactersLocalStorage(
        dao: CharactersDao,
        mapper: DatabaseMapper
    ): CharactersLocalStorage = CharactersLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideCharactersDao(database: AppDatabase): CharactersDao {
        return database.getCharactersDao()
    }

    @Singleton
    @Provides
    fun provideDatabaseMapper(): DatabaseMapper = DatabaseMapper()
}