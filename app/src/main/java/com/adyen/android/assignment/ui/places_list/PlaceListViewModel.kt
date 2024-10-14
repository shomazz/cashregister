package com.adyen.android.assignment.ui.places_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.R
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
) : ViewModel() {

    private val _state = MutableStateFlow<PlaceListState>(PlaceListState.Initial)
    val state: StateFlow<PlaceListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    fun loadNearbyPlaces() {
        viewModelScope.launch {
            _state.emit(PlaceListState.Loading)
            val result = placesRepository.getNearbyPlaces(52.376510, 4.905890)
            _state.emit(result.toState())
        }
    }

    fun refreshPlaces() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val result = placesRepository.getNearbyPlaces(52.376540, 4.905990)
            _isRefreshing.value = false
            _state.emit(result.toState())
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