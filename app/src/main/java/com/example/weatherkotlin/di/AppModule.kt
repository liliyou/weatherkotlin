package com.example.weatherkotlin.di

import android.content.Context
import androidx.room.Room
import com.example.search.domain.repository.SearchRepository
import com.example.weatherkotlin.BuildConfig
import com.example.weatherkotlin.data.local.CityWeatherDao
import com.example.weatherkotlin.data.local.SearchHistoryDao
import com.example.weatherkotlin.data.local.WeatherDatabase
import com.example.weatherkotlin.data.repository.LocationRepositoryImpl
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.data.repository.SearchRepositoryImpl
import com.example.weatherkotlin.data.repository.WeatherRepositoryImpl
import com.example.weatherkotlin.domain.repository.LocationRepository
import com.example.weatherkotlin.domain.repository.WeatherRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.weatherkotlin.data.remote.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.OPENWEATHER_API_KEY))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient, gson: Gson): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCityWeatherDao(database: WeatherDatabase): CityWeatherDao {
        return database.cityWeatherDao()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: WeatherDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}
