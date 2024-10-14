package com.adyen.android.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.domain.LocationRepository
import com.adyen.android.assignment.domain.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.Place
import com.adyen.android.assignment.rule.MainDispatcherRule
import com.adyen.android.assignment.ui.places_list.PlaceListState
import com.adyen.android.assignment.ui.places_list.PlaceListViewModel
import com.adyen.android.assignment.util.NetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PlaceListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placesRepository: NearbyPlacesRepository = mock()
    private val locationRepository: LocationRepository = mock()

    @Test
    fun `when fetch data successfully, then emit Success state`() = runTest {
        whenever(locationRepository.getLastLocation()).thenReturn(mock())
        whenever(placesRepository.getNearbyPlaces(any(), any()))
            .thenReturn(NetworkResult.Success(mockPlaces))

        val viewModel = createViewModel()

        assertTrue(viewModel.state.value is PlaceListState.Initial)

        viewModel.loadNearbyPlaces()

        assertTrue(viewModel.state.value is PlaceListState.Success)
        val successState = viewModel.state.value as PlaceListState.Success
        assertEquals(2, successState.places.size)
    }

    @Test
    fun `when unable to get location, then emit Error`() = runTest {
        whenever(locationRepository.getLastLocation()).thenReturn(null)

        val viewModel = createViewModel()
        viewModel.loadNearbyPlaces()

        assertTrue(viewModel.state.value is PlaceListState.Error)
        val errorState = viewModel.state.value as PlaceListState.Error
        assertEquals(R.string.error_location_not_found_title, errorState.headerRes)
    }

    @Test
    fun `when receive server error, then emit Error state`() = runTest {
        whenever(locationRepository.getLastLocation()).thenReturn(mock())
        whenever(placesRepository.getNearbyPlaces(any(), any()))
            .thenReturn(NetworkResult.Error(500, "Server Error"))

        val viewModel = createViewModel()
        viewModel.loadNearbyPlaces()

        assertTrue(viewModel.state.value is PlaceListState.Error)
        val errorState = viewModel.state.value as PlaceListState.Error
        assertEquals(R.string.error_service_unavailable_title, errorState.headerRes)
    }

    @Test
    fun `when retry successfully, then emit Success state`() = runTest {
        whenever(locationRepository.getLastLocation()).thenReturn(mock())
        whenever(placesRepository.getNearbyPlaces(any(), any()))
            .thenReturn(NetworkResult.Success(emptyList()))

        val viewModel = createViewModel()
        viewModel.loadNearbyPlaces()

        assertTrue(viewModel.state.value is PlaceListState.Error)
        val errorState = viewModel.state.value as PlaceListState.Error
        assertEquals(R.string.error_nothing_found_title, errorState.headerRes)

        whenever(placesRepository.getNearbyPlaces(any(), any()))
            .thenReturn(NetworkResult.Success(mockPlaces))
        viewModel.refreshPlaces()

        assertTrue(viewModel.state.value is PlaceListState.Success)
        val successState = viewModel.state.value as PlaceListState.Success
        assertEquals(2, successState.places.size)
    }


    private fun createViewModel() = PlaceListViewModel(
        placesRepository = placesRepository,
        locationRepository = locationRepository,
    )

}

private val mockPlaces = listOf(
    Place(
        id = "1",
        name = "Test",
        distance = 123,
        address = "Address 1",
        categories = emptyList()
    ),
    Place(
        id = "2",
        name = "Test",
        distance = 123,
        address = "Address 2",
        categories = emptyList()
    )
)