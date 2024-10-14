package com.adyen.android.assignment.data.location

import android.location.Location
import com.adyen.android.assignment.domain.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {


    override suspend fun getLastLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener { _ ->
                    continuation.resume(null)
                }
        }
    }

}