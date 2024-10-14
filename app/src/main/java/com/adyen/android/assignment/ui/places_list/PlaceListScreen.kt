package com.adyen.android.assignment.ui.places_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adyen.android.assignment.ui.places_list.compose.ErrorScreen
import com.adyen.android.assignment.ui.places_list.compose.InitialScreen
import com.adyen.android.assignment.ui.places_list.compose.LoadingScreen
import com.adyen.android.assignment.ui.places_list.compose.SuccessScreen
import com.adyen.android.assignment.ui.theme.NearbyPlacesTheme

@Composable
fun PlacesScreen(
    state: PlaceListState,
    isRefreshing: Boolean,
    startClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    NearbyPlacesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (state) {
                PlaceListState.Initial -> InitialScreen(startClick)
                PlaceListState.Loading -> LoadingScreen()
                is PlaceListState.Success -> SuccessScreen(state.places, isRefreshing, onRefresh)

                is PlaceListState.Error -> {
                    ErrorScreen(state.headerRes, state.descriptionRes, startClick)
                }
            }
        }
    }
}
