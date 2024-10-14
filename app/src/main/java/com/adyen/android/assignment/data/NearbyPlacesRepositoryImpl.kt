package com.adyen.android.assignment.data

import com.adyen.android.assignment.data.api.NearbyPlacesService
import com.adyen.android.assignment.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.Place
import com.adyen.android.assignment.util.DispatchersFactory
import com.adyen.android.assignment.util.NetworkResult
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class NearbyPlacesRepositoryImpl @Inject constructor(
    private val apiService: NearbyPlacesService,
    private val dispatchersFactory: DispatchersFactory,
) : NearbyPlacesRepository {

    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double
    ): NetworkResult<List<Place>> = withContext(dispatchersFactory.io()) {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude, longitude)
            .build()

        try {
            val response = apiService.getVenueRecommendations(query)
            if (response.isSuccessful) {
                val places = response.body()?.results?.mapNotNull(::map) ?: emptyList()
                NetworkResult.Success(places)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Throwable) {
            e.printStackTrace()
            NetworkResult.Exception(e)
        }
    }

}