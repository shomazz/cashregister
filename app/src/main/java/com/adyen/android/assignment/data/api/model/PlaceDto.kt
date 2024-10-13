package com.adyen.android.assignment.data.api.model

data class PlaceDto(
    val categories: List<CategoryDto>?,
    val distance: Int?,
    val geocodes: GeoCodeDto?,
    val location: LocationDto?,
    val name: String?,
    val timezone: String?,
)