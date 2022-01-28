package com.example.androidschool.data.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.data.database.CharactersDao
import com.example.androidschool.data.database.CharactersStorage
import com.example.androidschool.data.database.model.CharacterRoomEntity
import com.example.androidschool.data.database.model.toDomainModel
import com.example.androidschool.data.network.CharactersService
import com.example.androidschool.data.network.model.CharacterNetworkEntity
import com.example.androidschool.data.network.model.toDomainModel
import com.example.androidschool.domain.characters.model.CharacterEntity
import retrofit2.HttpException
import java.lang.Exception

private const val START_OFFSET = 0
private const val LIMIT = 10

class CharactersPagingSource(
    private val service: CharactersService,
    private val storage: CharactersStorage,
    private val action: () -> Unit
): PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val offset = params.key ?: START_OFFSET
        return try {
            val response = service.getCharactersPaginated(limit = LIMIT, offset = offset)

            if (response.isSuccessful) {
                val charactersNetwork = (response.body()!! as List<CharacterNetworkEntity>).map { it.toDomainModel() }
                Log.e("RESPONSE SUCCESSFUL", charactersNetwork.toString())

                storage.insertCharacters(charactersNetwork)

                val nextOffset = if (charactersNetwork.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = charactersNetwork,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            } else {
                Log.e("RESPONSE FAILED", "")

                val charactersLocal = (storage.getCharactersPaging(
                    limit = LIMIT,
                    offset = offset
                ) as List<CharacterRoomEntity> ).map { it.toDomainModel() }
                Log.e("EXTRACTION SUCCESS", charactersLocal.toString())

                val nextOffset = if (charactersLocal.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = charactersLocal,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            }
        } catch (e: Exception) {
            try {
                action()
                Log.e("FETCH FAILED", e.toString())
                val charactersLocal = (storage.getCharactersPaging(
                    limit = LIMIT,
                    offset = offset
                ) as List<CharacterEntity> )
                Log.e("EXTRACTION SUCCESS", charactersLocal.toString())

                val nextOffset = if (charactersLocal.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = charactersLocal,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            } catch (e: Exception) {
                Log.e("ALL FAILED", e.toString())
                action.invoke()
                LoadResult.Error(e)
            }
        }
//        return try {
//            val response = service.getCharactersPaginated(limit = LIMIT, offset = offset)
//            if (response.isSuccessful) {
//                val characters = (response.body()!! as List<CharacterNetworkEntity>).map { it.toDomainModel() }
//                val nextOffset = if (characters.size < LIMIT) null
//                else offset.plus(LIMIT)
//                LoadResult.Page(
//                    data = characters,
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            } else {
//                LoadResult.Error(response.errorBody() as HttpException)
//            }
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(LIMIT)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(LIMIT)
        }
    }
}