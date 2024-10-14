package com.adyen.android.assignment.ui.places_list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.adyen.android.assignment.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceListActivity : ComponentActivity() {

    private var pendingAction: PendingAction? = null

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pendingAction?.let { action ->
                    performAction(action)
                    pendingAction = null
                }
            } else {
                Toast.makeText(this, R.string.error_permission_is_required, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val viewModel: PlaceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()
            val isRefreshing by viewModel.isRefreshing.collectAsState()

            PlacesScreen(
                state,
                isRefreshing,
                startClick = remember(viewModel) { { withLocationPermissionCheck(PendingAction.LoadNearbyPlaces) } },
                onRefresh = remember(viewModel) { { withLocationPermissionCheck(PendingAction.RefreshPlaces) } },
            )
        }
    }

    private fun withLocationPermissionCheck(action: PendingAction) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            performAction(action)
        } else {
            pendingAction = action
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun performAction(action: PendingAction) {
        when (action) {
            is PendingAction.LoadNearbyPlaces -> viewModel.loadNearbyPlaces()
            is PendingAction.RefreshPlaces -> viewModel.refreshPlaces()
        }
    }

}

private sealed class PendingAction {
    data object LoadNearbyPlaces : PendingAction()
    data object RefreshPlaces : PendingAction()
}
