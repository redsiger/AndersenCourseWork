package com.example.androidschool.data.repositories.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.CharactersStorage
import com.example.androidschool.data.database.characters.model.CharacterRoomEntity
import com.example.androidschool.data.database.characters.model.toDomainModel
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.data.network.characters.model.toDomainModel
import com.example.androidschool.domain.characters.model.CharacterEntity
import java.lang.Exception

private const val START_OFFSET = 0
private const val LIMIT = 10

class CharactersPagingSource(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper,
    private val action: (text: String) -> Unit
): PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val offset = params.key ?: START_OFFSET
        return try {
            val response = service.getCharactersPaginated(limit = LIMIT, offset = offset)

            if (response.isSuccessful) {
                val characters = (response.body()!! as List<CharacterNetworkEntity>).map { it.toDomainModel() }

                dao.insertAll(characters.map { mapper.toRoomEntity(it) })

                val nextOffset = if (characters.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = characters,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            } else {
                val charactersLocal = (dao.getCharactersPaging(
                    limit = LIMIT,
                    offset = offset
                ) as List<CharacterRoomEntity> ).map { it.toDomainModel() }

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
                action("First catch + $e")
                val charactersLocal = (
                        dao.getCharactersPaging(limit = LIMIT, offset = offset
                ) as List<CharacterRoomEntity> ).map { it.toDomainModel() }

                val nextOffset = if (charactersLocal.size < LIMIT) null
                else offset.plus(LIMIT)

                LoadResult.Page(
                    data = charactersLocal,
                    prevKey = if (offset == START_OFFSET) null else offset.minus(LIMIT),
                    nextKey = nextOffset
                )
            } catch (e: Exception) {
                action("Second catch + $e")
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(LIMIT)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(LIMIT)
        }
    }
}