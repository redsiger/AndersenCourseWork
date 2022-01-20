package com.example.androidschool.feature_characters.data

import com.example.androidschool.feature_characters.data.network.CharactersService
import com.example.androidschool.feature_characters.data.network.model.toDomainModel
import com.example.androidschool.feature_characters.domain.CharactersRepository
import com.example.androidschool.feature_characters.domain.model.CharacterEntity

class CharactersRepositoryImpl(
    private val service: CharactersService
    ): CharactersRepository {

    private val pagingOffset = 10

    override suspend fun getCharactersPaging(): List<CharacterEntity> {
        val response = service.getCharactersPaginated(pagingOffset)
        return if (response.isSuccessful) {
            val charactersNetwork = response.body() ?: listOf()
            val charactersDomain = charactersNetwork.map { networkEntity ->
                networkEntity.toDomainModel()
            }
            charactersDomain
        } else {
            emptyList()
        }
    }
}