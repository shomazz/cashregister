package com.adyen.android.assignment.domain

import android.location.Location

interface LocationRepository {

    suspend fun getLastLocation(): Location?

}