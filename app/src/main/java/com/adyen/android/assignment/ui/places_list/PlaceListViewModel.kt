package com.adyen.android.assignment.ui.places_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.R
import com.adyen.android.assignment.domain.LocationRepository
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.Place
import com.adyen.android.assignment.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class PlaceListViewModel @Inject constructor(
    private val placesRepository: NearbyPlacesRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PlaceListState>(PlaceListState.Initial)
    val state: StateFlow<PlaceListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun loadNearbyPlaces() {
        viewModelScope.launch {
            _state.emit(PlaceListState.Loading)
            val result = getPlacesByLocation()
            _state.emit(result)
        }
    }

    fun refreshPlaces() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val result = getPlacesByLocation()
            _isRefreshing.value = false
            _state.emit(result)
        }
    }

    private suspend fun getPlacesByLocation(): PlaceListState {
        val location = locationRepository.getLastLocation()
        if (location == null) {
            return PlaceListState.Error(
                headerRes = R.string.error_location_not_found_title,
                descriptionRes = R.string.error_location_not_found_description
            )
        } else {
            val result = placesRepository.getNearbyPlaces(location.latitude, location.longitude)
            return result.toState()
        }
    }

    private fun NetworkResult<List<Place>>.toState(): PlaceListState {
        return when (this) {
            is NetworkResult.Success -> {
                if (data.isNotEmpty()) {
                    PlaceListState.Success(data)
                } else {
                    PlaceListState.Error(
                        R.string.error_nothing_found_title,
                        R.string.error_nothing_found_description,
                    )
                }
            }

            is NetworkResult.Error -> {
                when (code) {
                    in 400..499 -> PlaceListState.Error(
                        R.string.error_client_side_title,
                        R.string.error_client_side_description,
                    )

                    in 500..599 -> PlaceListState.Error(
                        R.string.error_service_unavailable_title,
                        R.string.error_service_unavailable_description,
                    )

                    else -> PlaceListState.Error(
                        R.string.error_connection_problem_title,
                        R.string.error_connection_problem_description,
                    )
                }
            }

            is NetworkResult.Exception -> {
                when (exception) {
                    is IOException -> PlaceListState.Error(
                        R.string.error_no_internet_title,
                        R.string.error_no_internet_description,
                    )

                    else -> PlaceListState.Error(
                        R.string.error_unexpected_title,
                        R.string.error_unexpected_description,
                    )
                }
            }
        }
    }

}