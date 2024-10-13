package com.adyen.android.assignment.data

import com.adyen.android.assignment.data.api.NearbyPlacesService
import com.adyen.android.assignment.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.Place
import com.adyen.android.assignment.util.NetworkResult
import retrofit2.HttpException
import javax.inject.Inject

class NearbyPlacesRepositoryImpl @Inject constructor(
    //TODO: provide dispatchers
    //TODO: catch internet connection loss
    private val apiService: NearbyPlacesService,
) : NearbyPlacesRepository {

    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double
    ): NetworkResult<List<Place>> {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude, longitude)
            .build()

        return try {
            val response = apiService.getVenueRecommendations(query)
            if (response.isSuccessful) {
                val places = response.body()?.results?.map(::map) ?: emptyList()
                NetworkResult.Success(places)
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: HttpException) {
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }

}