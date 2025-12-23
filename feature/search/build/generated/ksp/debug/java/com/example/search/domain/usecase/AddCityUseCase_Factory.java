package com.example.search.domain.usecase;

import com.example.search.domain.repository.SearchRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AddCityUseCase_Factory implements Factory<AddCityUseCase> {
  private final Provider<SearchRepository> repositoryProvider;

  public AddCityUseCase_Factory(Provider<SearchRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddCityUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddCityUseCase_Factory create(Provider<SearchRepository> repositoryProvider) {
    return new AddCityUseCase_Factory(repositoryProvider);
  }

  public static AddCityUseCase newInstance(SearchRepository repository) {
    return new AddCityUseCase(repository);
  }
}
