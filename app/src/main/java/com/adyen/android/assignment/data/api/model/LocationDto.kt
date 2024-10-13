package com.adyen.android.assignment.data.api.model

import com.squareup.moshi.Json

data class LocationDto(
    val address: String?,
    val country: String?,
    @Json(name = "formatted_address")
    val formattedAddress: String?,
    val locality: String?,
    val neighbourhood: List<String>?,
    val postcode: String?,
    val region: String?,
)
