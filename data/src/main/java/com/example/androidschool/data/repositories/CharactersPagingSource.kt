package com.example.androidschool.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.data.network.CharactersService
import com.example.androidschool.data.network.model.CharacterNetworkEntity
import com.example.androidschool.data.network.model.toDomainModel
import com.example.androidschool.domain.characters.model.CharacterEntity
import retrofit2.HttpException
import java.lang.Exception

private const val START_OFFSET = 0
private const val LIMIT = 10

class CharactersPagingSource(
    private val service: CharactersService
): PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val offset = params.key ?: START_OFFSET
        return try {
            val response = service.getCharactersPaginated(limit = LIMIT, offset = offset)
            if (response.isSuccessful) {
                val characters = (response.body()!! as List<CharacterNetworkEntity>).map { it.toDomainModel() }
                val nextOffset = if (characters.size < LIMIT) null
                else offset.plus(LIMIT)
                LoadResult.Page(
                    data = characters,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            } else {
                LoadResult.Error(response.errorBody() as HttpException)
            }
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