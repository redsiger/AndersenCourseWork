package com.example.androidschool.andersencoursework.ui.characters.list

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.data.repositories.characters.LoadCharactersAction
import com.example.androidschool.util.Status
import java.lang.Exception

private const val START_OFFSET = 0
private const val LIMIT = 10

class CharactersPagingSource(
    private val loader: LoadCharactersAction,
    private val mapper: UIMapper
): PagingSource<Int, CharacterUIEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterUIEntity> {
        val offset = params.key ?: START_OFFSET
        return when (val result = loader(LIMIT, offset)) {
            is Status.Success -> {
                val nextOffset = if (result.data.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = result.data.map(mapper::mapCharacterEntity),
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            }
            is Status.Error -> {
                LoadResult.Error(result.exception)
            }
            else -> {
                Log.e("PAGING",result.toString())
                LoadResult.Error(Exception(result.toString()))
            }
        }
    }

    //    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
//        val offset = params.key ?: START_OFFSET
//        return try {
//            val response = service.getCharactersPaginated(limit = LIMIT, offset = offset)
//
//            if (response.isSuccessful) {
//                val characters = (response.body()!! as List<CharacterNetworkEntity>).map(CharacterNetworkEntity::toDomainModel)
//
//                dao.insertAll(characters.map { mapper.toRoomEntity(it) })
//
//                val nextOffset = if (characters.size < LIMIT) null
//                else offset.plus(LIMIT)
//
//                LoadResult.Page(
//                    data = characters,
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            } else {
//                val charactersLocal = (dao.getCharactersPaging(
//                    limit = LIMIT,
//                    offset = offset
//                ) as List<CharacterRoomEntity> ).map(CharacterRoomEntity::toDomainModel)
//
//                val nextOffset = if (charactersLocal.size < LIMIT) null
//                else offset.plus(LIMIT)
//
//                LoadResult.Page(
//                    data = charactersLocal,
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            }
//        } catch (e: Exception) {
//            try {
//                action("First catch + $e")
//                val charactersLocal = (
//                        dao.getCharactersPaging(limit = LIMIT, offset = offset
//                ) as List<CharacterRoomEntity> ).map(CharacterRoomEntity::toDomainModel)
//
//                val nextOffset = if (charactersLocal.size < LIMIT) null
//                else offset.plus(LIMIT)
//
//                LoadResult.Page(
//                    data = charactersLocal,
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            } catch (e: Exception) {
//                action("Second catch + $e")
//                LoadResult.Error(e)
//            }
//        }
//    }

    override fun getRefreshKey(state: PagingState<Int, CharacterUIEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(LIMIT)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(LIMIT)
        }
    }
}