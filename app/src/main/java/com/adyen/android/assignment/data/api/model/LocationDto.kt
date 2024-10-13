package com.adyen.android.assignment.data.api.model

data class LocationDto(
    val address: String?,
    val country: String?,
    val locality: String?,
    val neighbourhood: List<String>?,
    val postcode: String?,
    val region: String?,
)
