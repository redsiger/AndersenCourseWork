package com.example.androidschool.data.repositories.characters

import android.content.Context
import android.widget.Toast
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.loaders.CharactersLoader
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.characters.model.CharacterNetworkEntity
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.model.CharacterAttr
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.domain.characters.model.PagingAttr
import com.example.androidschool.util.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import kotlin.Exception

typealias LoadCharactersAction = suspend (limit: Int, offset: Int) -> Status<List<CharacterEntity>>

class CharactersRepositoryImpl(
    private val service: CharactersService,
    private val dao: CharactersDao,
    private val mapper: DatabaseMapper,
    private val context: Context
    ): CharactersRepository {

    private val loader = CharactersLoader(service, dao, mapper)

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun searchCharactersByNameOrNickName(query: String): Flow<List<CharacterEntity>> {
        return dao.searchCharactersByNameOrNickname(query).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun getCharactersPaging(attr: PagingAttr): Status<List<CharacterEntity>> {
        return loader.getCharacterPaging(attr.limit, attr.offset)
    }

    override suspend fun getCharacter(characterAttr: CharacterAttr): Status<CharacterEntity> {
        return try {
            val characterId = characterAttr.id
            val response = service.getCharacter(characterId)
            if (response.isSuccessful) {
                Status.Success(response.body()!!.map(CharacterNetworkEntity::toDomainModel).first())
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }
}