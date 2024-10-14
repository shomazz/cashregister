package com.adyen.android.assignment.di

import com.adyen.android.assignment.data.NearbyPlacesRepositoryImpl
import com.adyen.android.assignment.data.api.NearbyPlacesService
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.util.DispatchersFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideNearbyPlacesRepository(
        placesApiService: NearbyPlacesService,
        dispatchersFactory: DispatchersFactory,
    ): NearbyPlacesRepository {
        return NearbyPlacesRepositoryImpl(
            placesApiService,
            dispatchersFactory,
        )
    }

}