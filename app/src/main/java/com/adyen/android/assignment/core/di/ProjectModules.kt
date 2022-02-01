package com.adyen.android.assignment.core.di

import android.app.Application
import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.data.RemoteDataSource
import com.adyen.android.assignment.data.RepositoryImp
import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.domain.repo.Repository
import com.adyen.android.assignment.presentation.displayVenues.LocationLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ProjectModules {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        placesService: PlacesService
    ): RemoteDataSource {
        return RemoteDataSource(placesService)
    }

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ): Repository {
        return RepositoryImp(remoteDataSource)
    }

    @Singleton
    @Provides
    fun provideLocationLiveDataNew(
        application:Application
    ): LocationLiveData {
        return LocationLiveData(application)
    }

    //PlacesService modules
    @Singleton
    @Provides
    fun provideRetrofit(
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): PlacesService = retrofit.create(PlacesService::class.java)

}
