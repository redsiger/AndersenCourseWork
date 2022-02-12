package com.example.androidschool.data.di

import android.content.Context
import com.example.androidschool.data.database.*
import com.example.androidschool.data.database.characters.CharacterDetailsDao
import com.example.androidschool.data.database.characters.CharactersListDao
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsLocalStorage
import com.example.androidschool.data.repositories.characters.list.CharactersLocalStorage
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
    fun provideCharacterDetailsLocalStorage(
        dao: CharacterDetailsDao,
        mapper: DatabaseMapper
    ): CharacterDetailsLocalStorage = CharacterDetailsLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideCharacterDetailsDao(database: AppDatabase): CharacterDetailsDao
        = database.getCharacterDetailsDao()

    @Singleton
    @Provides
    fun provideCharactersLocalStorage(
        dao: CharactersListDao,
        mapper: DatabaseMapper
    ): CharactersLocalStorage = CharactersLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideCharactersDao(database: AppDatabase): CharactersListDao
        = database.getCharactersDao()

    @Singleton
    @Provides
    fun provideDatabaseMapper(): DatabaseMapper = DatabaseMapper()
}