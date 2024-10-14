package com.adyen.android.assignment.ui.places_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceListActivity : ComponentActivity() {

    private val viewModel: PlaceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()
            val isRefreshing by viewModel.isRefreshing.collectAsState()

            PlacesScreen(
                state, isRefreshing,
                remember(viewModel) { viewModel::loadNearbyPlaces },
                remember(viewModel) { viewModel::refreshPlaces },
            )
        }
    }

}
