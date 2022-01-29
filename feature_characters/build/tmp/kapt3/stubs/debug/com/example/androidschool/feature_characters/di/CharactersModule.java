package com.example.androidschool.feature_characters.di;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004H\u0007\u00a8\u0006\r"}, d2 = {"Lcom/example/androidschool/feature_characters/di/CharactersModule;", "", "()V", "provideCharactersRepository", "Lcom/example/androidschool/feature_characters/domain/CharactersRepository;", "service", "Lcom/example/androidschool/feature_characters/data/network/CharactersService;", "provideCharactersService", "retrofit", "Lretrofit2/Retrofit;", "provideGetCharactersPagingUseCase", "Lcom/example/androidschool/feature_characters/domain/usecase/GetCharactersPagingUseCase;", "repository", "feature_characters_debug"})
@dagger.Module()
public final class CharactersModule {
    
    public CharactersModule() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    @javax.inject.Singleton()
    public final com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase provideGetCharactersPagingUseCase(@org.jetbrains.annotations.NotNull()
    com.example.androidschool.feature_characters.domain.CharactersRepository repository) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    @javax.inject.Singleton()
    public final com.example.androidschool.feature_characters.domain.CharactersRepository provideCharactersRepository(@org.jetbrains.annotations.NotNull()
    com.example.androidschool.feature_characters.data.network.CharactersService service) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    @javax.inject.Singleton()
    public final com.example.androidschool.feature_characters.data.network.CharactersService provideCharactersService(@org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
}