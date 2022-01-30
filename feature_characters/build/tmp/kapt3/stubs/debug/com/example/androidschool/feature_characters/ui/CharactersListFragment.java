package com.example.androidschool.feature_characters.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J&\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\u001a\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\""}, d2 = {"Lcom/example/androidschool/feature_characters/ui/CharactersListFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/androidschool/feature_characters/databinding/FragmentCharactersListBinding;", "charactersComponent", "Lcom/example/androidschool/feature_characters/di/CharactersComponent;", "getCharactersComponent", "()Lcom/example/androidschool/feature_characters/di/CharactersComponent;", "setCharactersComponent", "(Lcom/example/androidschool/feature_characters/di/CharactersComponent;)V", "getCharactersUseCase", "Lcom/example/androidschool/feature_characters/domain/usecase/GetCharactersPagingUseCase;", "getGetCharactersUseCase", "()Lcom/example/androidschool/feature_characters/domain/usecase/GetCharactersPagingUseCase;", "setGetCharactersUseCase", "(Lcom/example/androidschool/feature_characters/domain/usecase/GetCharactersPagingUseCase;)V", "viewBinding", "getViewBinding", "()Lcom/example/androidschool/feature_characters/databinding/FragmentCharactersListBinding;", "onAttach", "", "context", "Landroid/content/Context;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "feature_characters_debug"})
public final class CharactersListFragment extends androidx.fragment.app.Fragment {
    public com.example.androidschool.feature_characters.di.CharactersComponent charactersComponent;
    @javax.inject.Inject()
    public com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase getCharactersUseCase;
    private com.example.androidschool.feature_characters.databinding.FragmentCharactersListBinding _binding;
    
    public CharactersListFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.androidschool.feature_characters.di.CharactersComponent getCharactersComponent() {
        return null;
    }
    
    public final void setCharactersComponent(@org.jetbrains.annotations.NotNull()
    com.example.androidschool.feature_characters.di.CharactersComponent p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase getGetCharactersUseCase() {
        return null;
    }
    
    public final void setGetCharactersUseCase(@org.jetbrains.annotations.NotNull()
    com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase p0) {
    }
    
    private final com.example.androidschool.feature_characters.databinding.FragmentCharactersListBinding getViewBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onAttach(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}