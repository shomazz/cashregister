package com.adyen.android.assignment.domain

import com.adyen.android.assignment.domain.model.Place
import com.adyen.android.assignment.util.NetworkResult

interface NearbyPlacesRepository {

    suspend fun getNearbyPlaces(latitude: Double, longitude: Double): NetworkResult<List<Place>>

}