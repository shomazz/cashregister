package com.adyen.android.assignment.data.api.model

data class GeoCodeDto(
    val main: MainDto?,
)

data class MainDto(
    val latitude: Double?,
    val longitude: Double?,
)