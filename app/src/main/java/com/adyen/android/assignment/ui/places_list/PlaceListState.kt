package com.adyen.android.assignment.ui.places_list

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.adyen.android.assignment.domain.model.Place

sealed class PlaceListState {

    data object Initial : PlaceListState()
    data object Loading : PlaceListState()

    @Immutable
    data class Success(val places: List<Place>) : PlaceListState()

    @Immutable
    data class Error(
        @StringRes val headerRes: Int,
        @StringRes val descriptionRes: Int
    ) : PlaceListState()

}