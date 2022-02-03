package com.example.androidschool.data.mediators

import android.util.Log
import androidx.paging.*
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.characters.model.CharactersRemoteKeys
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.CharactersEntityRemoteKeys
import com.example.androidschool.util.Status
import javax.inject.Inject

private const val START_OFFSET = 0
private const val LIMIT = 10

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor (
    private val interactor: CharactersInteractor,
    private val mapper: DatabaseMapper
): RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val offset: Int = when (loadType) {
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.e("CharactersRemoteMediator","load/APPEND remoteKeys:$remoteKeys")
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.e("CharactersRemoteMediator","load/PREPEND remoteKeys:$remoteKeys")
                prevKey
            }
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.e("CharactersRemoteMediator","load/REFRESH remoteKeys:$remoteKeys")
                remoteKeys?.nextKey?.minus(10) ?: START_OFFSET
            }
        }

        val response = interactor.getRemoteCharactersPaging(offset, state.config.pageSize)
        Log.e("CharactersRemoteMediator","remote load offset:$offset size:${response.extractData?.size} endPagination:${response.extractData?.size!! < state.config.pageSize} response:$response")

        return when (response) {
            is Status.Success -> {
                val data = response.data
                val endOfPagination = data.size < state.config.pageSize

                if (loadType == LoadType.REFRESH) {
                    Log.e("CharactersRemoteMediator", "wiping data and keys offset:$offset ")
                    interactor.clearCharactersWithRemoteKeys()
                }

                val prevKey = if (offset == START_OFFSET) null else offset - LIMIT
                val nextKey = if (endOfPagination) null else offset + LIMIT
                val keys = data.map {
                    CharactersEntityRemoteKeys(it.charId, prevKey, nextKey)
                }
                Log.e("CharactersRemoteMediator", "inserting keys offset:$offset keys:$keys ")
                interactor.insertCharactersRemoteKeys(keys)
                Log.e("CharactersRemoteMediator", "inserting remote data offset:$offset data:$data")
                interactor.insertCharacters(data)

                MediatorResult.Success(endOfPaginationReached = (response.data.size < state.config.pageSize))
            }
            is Status.Error -> MediatorResult.Error(response.exception)
            else -> MediatorResult.Error(Exception("Something went wrong"))
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): CharactersRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { characterEntity ->
                // Get the remote keys of the last item retrieved
                mapper.toRoomEntity(interactor.remoteKeysCharacters(characterEntity.charId))
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): CharactersRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { characterEntity ->
                // Get the remote keys of the first items retrieved
                mapper.toRoomEntity(interactor.remoteKeysCharacters(characterEntity.charId))
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>
    ): CharactersRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.charId?.let { charId ->
                mapper.toRoomEntity(interactor.remoteKeysCharacters(charId))
            }
        }
    }
}