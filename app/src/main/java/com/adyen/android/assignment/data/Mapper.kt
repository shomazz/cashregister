package com.adyen.android.assignment.data

import com.adyen.android.assignment.data.api.model.CategoryDto
import com.adyen.android.assignment.data.api.model.IconDto
import com.adyen.android.assignment.data.api.model.PlaceDto
import com.adyen.android.assignment.domain.model.Category
import com.adyen.android.assignment.domain.model.Place

internal fun map(dto: PlaceDto): Place = with(dto) {
    Place(
        id = id,
        name = name,
        distance = distance,
        address = location?.formattedAddress,
        categories = categories?.map(::map) ?: emptyList()
    )
}

internal fun map(dto: CategoryDto): Category = with(dto) {
    Category(
        id = id,
        name = name,
        iconUrl = icon?.let(::map)
    )
}

internal fun map(dto: IconDto): String? = with(dto) {
    if (prefix != null && suffix != null) {
        "${prefix}${ICON_SIZE}${suffix}"
    } else {
        null
    }
}

private const val ICON_SIZE = 64