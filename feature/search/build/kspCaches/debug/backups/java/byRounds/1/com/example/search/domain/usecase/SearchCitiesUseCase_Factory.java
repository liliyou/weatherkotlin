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
public final class SearchCitiesUseCase_Factory implements Factory<SearchCitiesUseCase> {
  private final Provider<SearchRepository> repositoryProvider;

  public SearchCitiesUseCase_Factory(Provider<SearchRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchCitiesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchCitiesUseCase_Factory create(Provider<SearchRepository> repositoryProvider) {
    return new SearchCitiesUseCase_Factory(repositoryProvider);
  }

  public static SearchCitiesUseCase newInstance(SearchRepository repository) {
    return new SearchCitiesUseCase(repository);
  }
}
