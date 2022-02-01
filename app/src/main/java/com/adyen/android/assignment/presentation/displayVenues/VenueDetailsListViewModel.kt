package com.adyen.android.assignment.presentation.displayVenues

import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.core.Constants
import com.adyen.android.assignment.core.NetworkResult
import com.adyen.android.assignment.data.api.entity.toVenueDetailsList
import com.adyen.android.assignment.domain.model.VenueDetail
import com.adyen.android.assignment.domain.usecase.GetVenueDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsListViewModel @Inject constructor(
    private val locationLiveData: LocationLiveData,
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase
) :
    ViewModel() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _venueDetails = MutableLiveData<NetworkResult<List<VenueDetail>?>>()
    var venueDetails: LiveData<NetworkResult<List<VenueDetail>?>> = _venueDetails
    private var searchJob: Job? = null


    val locationData: LiveData<Location> = locationLiveData

    fun getVenueDetailsFromServer(query: Map<String, String>) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {

            _venueDetails.value = NetworkResult.Loading()

            getVenueDetailsUseCase(query)
                .catch {
                    _venueDetails.value = NetworkResult.Error(
                        message = Constants.INTERNET_ERROR
                    )

                }
                .collect {
                    if (it.isSuccessful) {
                        _venueDetails.value =
                            NetworkResult.Success(it.body()?.toVenueDetailsList())

                    } else {
                        _venueDetails.value =
                            NetworkResult.Error(message = Constants.ERROR_FROM_SERVER)
                    }
                }

        }


    }
    

}