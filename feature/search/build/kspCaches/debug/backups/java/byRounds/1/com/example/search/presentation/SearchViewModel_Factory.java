package com.example.search.presentation;

import com.example.search.domain.usecase.AddCityUseCase;
import com.example.search.domain.usecase.GetSuggestedCitiesUseCase;
import com.example.search.domain.usecase.SearchCitiesUseCase;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<SearchCitiesUseCase> searchCitiesUseCaseProvider;

  private final Provider<AddCityUseCase> addCityUseCaseProvider;

  private final Provider<GetSuggestedCitiesUseCase> getSuggestedCitiesUseCaseProvider;

  public SearchViewModel_Factory(Provider<SearchCitiesUseCase> searchCitiesUseCaseProvider,
      Provider<AddCityUseCase> addCityUseCaseProvider,
      Provider<GetSuggestedCitiesUseCase> getSuggestedCitiesUseCaseProvider) {
    this.searchCitiesUseCaseProvider = searchCitiesUseCaseProvider;
    this.addCityUseCaseProvider = addCityUseCaseProvider;
    this.getSuggestedCitiesUseCaseProvider = getSuggestedCitiesUseCaseProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(searchCitiesUseCaseProvider.get(), addCityUseCaseProvider.get(), getSuggestedCitiesUseCaseProvider.get());
  }

  public static SearchViewModel_Factory create(
      Provider<SearchCitiesUseCase> searchCitiesUseCaseProvider,
      Provider<AddCityUseCase> addCityUseCaseProvider,
      Provider<GetSuggestedCitiesUseCase> getSuggestedCitiesUseCaseProvider) {
    return new SearchViewModel_Factory(searchCitiesUseCaseProvider, addCityUseCaseProvider, getSuggestedCitiesUseCaseProvider);
  }

  public static SearchViewModel newInstance(SearchCitiesUseCase searchCitiesUseCase,
      AddCityUseCase addCityUseCase, GetSuggestedCitiesUseCase getSuggestedCitiesUseCase) {
    return new SearchViewModel(searchCitiesUseCase, addCityUseCase, getSuggestedCitiesUseCase);
  }
}
