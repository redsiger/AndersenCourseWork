// Generated by Dagger (https://dagger.dev).
package com.example.androidschool.feature_characters.di;

import com.example.androidschool.feature_characters.data.network.CharactersService;
import com.example.androidschool.feature_characters.domain.CharactersRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CharactersModule_ProvideCharactersRepositoryFactory implements Factory<CharactersRepository> {
  private final CharactersModule module;

  private final Provider<CharactersService> serviceProvider;

  public CharactersModule_ProvideCharactersRepositoryFactory(CharactersModule module,
      Provider<CharactersService> serviceProvider) {
    this.module = module;
    this.serviceProvider = serviceProvider;
  }

  @Override
  public CharactersRepository get() {
    return provideCharactersRepository(module, serviceProvider.get());
  }

  public static CharactersModule_ProvideCharactersRepositoryFactory create(CharactersModule module,
      Provider<CharactersService> serviceProvider) {
    return new CharactersModule_ProvideCharactersRepositoryFactory(module, serviceProvider);
  }

  public static CharactersRepository provideCharactersRepository(CharactersModule instance,
      CharactersService service) {
    return Preconditions.checkNotNullFromProvides(instance.provideCharactersRepository(service));
  }
}