package com.example.androidschool.feature_characters.domain.usecase

import com.example.androidschool.feature_characters.domain.CharactersRepository
import com.example.androidschool.feature_characters.domain.model.CharacterEntity

class GetCharactersPagingUseCase(private val repository: CharactersRepository) {
    suspend fun execute(): List<CharacterEntity> {
        return repository.getCharactersPaging()
    }
}