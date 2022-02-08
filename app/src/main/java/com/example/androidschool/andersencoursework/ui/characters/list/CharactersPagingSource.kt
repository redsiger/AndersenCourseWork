package com.example.androidschool.andersencoursework.ui.characters.list

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.domain.characters.model.CharacterEntity
import java.lang.Exception

private const val START_OFFSET = 0
private const val LIMIT = 10

class CharactersPagingSourceForMediator(
    private val daoAction: (offset: Int, limit: Int) -> List<CharacterEntity>,
    private val mapper: UIMapper
): PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val offset = params.key ?: START_OFFSET
        return try {
            val charactersList = daoAction(offset, LIMIT)

            Log.e("CharactersPagingSourceForMediator","local load offset:$offset local charactersList:$charactersList")

            val nextOffset = if (charactersList.size < LIMIT) null
            else offset.plus(LIMIT)

            val prevOffset = if (offset == START_OFFSET) null
            else offset.minus(LIMIT)

            Log.e("CharactersPagingSourceForMediator","local load offset:$offset nextOffset:$nextOffset, prevOffset:$prevOffset")

            LoadResult.Page(
                data = charactersList,
                prevKey = prevOffset,
                nextKey = nextOffset
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(LIMIT)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(LIMIT)
        }
    }

}

//class CharactersPagingSource(
//    private val loader: LoadCharactersAction,
//    private val mapper: UIMapper,
//    private val onLocal: () -> Unit
//): PagingSource<Int, CharacterUIEntity>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterUIEntity> {
//        val offset = params.key ?: START_OFFSET
//        return when (val result = loader(LIMIT, offset)) {
//            is Status.Success -> {
//                val nextOffset = if (result.data.size < LIMIT) null
//                else offset.plus(LIMIT)
//
//                LoadResult.Page(
//                    data = result.data.map(mapper::mapCharacterEntity),
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            }
//            is Status.Success -> {
//                onLocal()
//                val nextOffset = if (result.data.size < LIMIT) null
//                else offset.plus(LIMIT)
//
//                LoadResult.Page(
//                    data = result.data.map(mapper::mapCharacterEntity),
//                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
//                    nextKey = nextOffset
//                )
//            }
//            is Status.Error -> {
//                LoadResult.Error(result.exception)
//            }
//            else -> {
//                Log.e("PAGING",result.toString())
//                LoadResult.Error(Exception(result.toString()))
//            }
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, CharacterUIEntity>): Int? {
//        return state.anchorPosition?.let {
//            state.closestPageToPosition(it)?.prevKey?.plus(LIMIT)
//                ?: state.closestPageToPosition(it)?.nextKey?.minus(LIMIT)
//        }
//    }
//}