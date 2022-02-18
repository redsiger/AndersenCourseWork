package com.example.androidschool.data.database

import android.content.Context
import androidx.room.*
import com.example.androidschool.data.database.characters.CharacterDetailsDao
import com.example.androidschool.data.database.characters.CharactersListDao
import com.example.androidschool.data.database.characters.model.CharacterDetailsRoom
import com.example.androidschool.data.database.characters.model.CharacterInEpisodeRoom
import com.example.androidschool.data.database.characters.model.CharacterListItemRoom
import com.example.androidschool.data.database.episodes.EpisodeDetailsDao
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.database.episodes.model.EpisodeDetailsRoom
import com.example.androidschool.data.database.episodes.model.EpisodeListItemRoom

@Database(
    entities = [
        CharacterListItemRoom::class,
        CharacterDetailsRoom::class,
        CharacterInEpisodeRoom::class,
        EpisodeDetailsRoom::class,
        EpisodeListItemRoom::class],
    version = 1,
    exportSchema = true
)
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

    abstract fun getCharactersDao(): CharactersListDao
    abstract fun getCharacterDetailsDao(): CharacterDetailsDao

    abstract fun getEpisodesDao(): EpisodesListDao
    abstract fun getEpisodeDetailsDao(): EpisodeDetailsDao
}