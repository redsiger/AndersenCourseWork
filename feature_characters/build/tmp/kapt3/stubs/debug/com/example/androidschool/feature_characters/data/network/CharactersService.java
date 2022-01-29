package com.example.androidschool.feature_characters.data.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J1\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\b\u0003\u0010\u0006\u001a\u00020\u00072\b\b\u0003\u0010\b\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/example/androidschool/feature_characters/data/network/CharactersService;", "", "getCharactersPaginated", "Lretrofit2/Response;", "", "Lcom/example/androidschool/feature_characters/data/network/model/CharacterNetworkEntity;", "limit", "", "offset", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "feature_characters_debug"})
public abstract interface CharactersService {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "characters")
    public abstract java.lang.Object getCharactersPaginated(@retrofit2.http.Query(value = "limit")
    int limit, @retrofit2.http.Query(value = "offset")
    int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.androidschool.feature_characters.data.network.model.CharacterNetworkEntity>>> continuation);
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 3)
    public final class DefaultImpls {
    }
}