package com.example.androidschool.data.repositories

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.androidschool.data.database.CharactersDao
import com.example.androidschool.data.database.CharactersStorage
import com.example.androidschool.data.network.CharactersService
import com.example.androidschool.data.network.model.CharacterNetworkAttr
import com.example.androidschool.data.network.model.toDomainModel
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import retrofit2.HttpException
import kotlin.Exception

class CharactersRepositoryImpl(
    private val service: CharactersService,
    private val storage: CharactersStorage,
    private val context: Context
    ): CharactersRepository {

    fun showToast() {
        Toast.makeText(context, "Oops...", Toast.LENGTH_LONG).show()
    }

    override suspend fun getCharactersPaging(): LiveData<PagingData<CharacterEntity>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { CharactersPagingSource(service, storage, ::showToast) }
    ).liveData


//    override suspend fun getCharactersPaging(): Status<List<CharacterEntity>> {
//        return try {
//            val response = service.getCharactersPaginated()
//            if (response.isSuccessful) {
//                Status.Success(response.body()!!.map { it.toDomainModel() })
//            } else {
//                Status.Error(response.errorBody() as HttpException)
//            }
//        } catch (e: Exception) {
//            return  Status.Error(e)
//        }
//    }

    override suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity> {
        return try {
            val characterId = CharacterNetworkAttr(characterAttr.id).id
            val response = service.getCharacter(characterId)
            if (response.isSuccessful) {
                Status.Success(response.body()!!.map { it.toDomainModel() }.first())
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }
}