package com.example.androidschool.data.di

import android.content.Context
import com.example.androidschool.data.database.*
import com.example.androidschool.data.database.characters.CharacterDetailsDao
import com.example.androidschool.data.database.characters.CharactersListDao
import com.example.androidschool.data.database.episodes.EpisodeDetailsDao
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsLocalStorage
import com.example.androidschool.data.repositories.characters.list.CharactersListLocalStorage
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context, typeConverters: RoomTypeConverters): AppDatabase =
        AppDatabase.getInstance(context, typeConverters)

    @Provides
    fun provideRoomTypeConverters(moshi: Moshi): RoomTypeConverters =
        RoomTypeConverters(moshi)

    @Singleton
    @Provides
    fun provideCharacterDetailsLocalStorage(
        dao: CharacterDetailsDao,
        mapper: DatabaseMapper
    ): CharacterDetailsLocalStorage = CharacterDetailsLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideCharacterDetailsDao(database: AppDatabase): CharacterDetailsDao =
        database.getCharacterDetailsDao()

    @Singleton
    @Provides
    fun provideCharactersLocalStorage(
        dao: CharactersListDao,
        mapper: DatabaseMapper
    ): CharactersListLocalStorage = CharactersListLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideCharactersDao(database: AppDatabase): CharactersListDao =
        database.getCharactersDao()

    @Singleton
    @Provides
    fun provideEpisodeListDao(database: AppDatabase): EpisodesListDao =
        database.getEpisodesDao()

    @Singleton
    @Provides
    fun provideEpisodeDetailsDao(database: AppDatabase): EpisodeDetailsDao =
        database.getEpisodeDetailsDao()

    @Singleton
    @Provides
    fun provideDatabaseMapper(): DatabaseMapper = DatabaseMapper()
}