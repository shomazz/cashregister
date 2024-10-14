package com.adyen.android.assignment.data.api.model

import com.squareup.moshi.Json

data class PlaceDto(
    @Json(name = "fsq_id")
    val id: String?,
    val categories: List<CategoryDto>?,
    val distance: Int?,
    val location: LocationDto?,
    val name: String?,
)