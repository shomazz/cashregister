package com.adyen.android.assignment.di

import com.adyen.android.assignment.data.NearbyPlacesRepositoryImpl
import com.adyen.android.assignment.data.api.NearbyPlacesService
import com.adyen.android.assignment.data.location.LocationRepositoryImpl
import com.adyen.android.assignment.domain.LocationRepository
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.util.DispatchersFactory
import com.google.android.gms.location.FusedLocationProviderClient
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

    @Provides
    fun provideLocationRepository(
        fusedLocationClient: FusedLocationProviderClient
    ): LocationRepository {
        return LocationRepositoryImpl(fusedLocationClient)
    }

}