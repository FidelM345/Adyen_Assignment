package com.adyen.android.assignment.presentation.displayVenues

import com.adyen.android.assignment.core.NetworkResult
import com.adyen.android.assignment.domain.model.VenueDetail
import com.adyen.android.assignment.domain.usecase.GetVenueDetailsUseCase
import com.example.androidplaylist.utils.BaseunitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest
import java.lang.RuntimeException

class VenueDetailsListViewModelTest : BaseunitTest() {
    private val locationLiveData: LocationLiveData = mock()//mocking the repo class
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase = mock()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun `venue details live data should emit success case if response from api is successful`() =
        runBlockingTest {
            val venueDetailsListViewModel =
                VenueDetailsListViewModel(locationLiveData, getVenueDetailsUseCase)

            val dummyResults: NetworkResult<List<VenueDetail>?> = NetworkResult.Success(emptyList())

            venueDetailsListViewModel._venueDetails.value = dummyResults
            //the capture command is also from the liveData util file
            venueDetailsListViewModel.venueDetails.captureValues {

                //we force the live data to emit results using the getValueForTest()
                venueDetailsListViewModel.venueDetails.getValueForTest()
                //values array is from the LiveData Util file
                //we the assert that the first value emitted is true
                assertEquals(dummyResults, values[0])
            }


        }

    @Test
    fun `venue details live data should emit Error case if response from api is NotSuccessful`() =
        runBlockingTest {
            val venueDetailsListViewModel =
                VenueDetailsListViewModel(locationLiveData, getVenueDetailsUseCase)

            val dummyResults: NetworkResult<List<VenueDetail>?> = NetworkResult.Error("Network Error")

            venueDetailsListViewModel._venueDetails.value = dummyResults
            //the capture command is also from the liveData util file
            venueDetailsListViewModel.venueDetails.captureValues {

                //we force the live data to emit results using the getValueForTest()
                venueDetailsListViewModel.venueDetails.getValueForTest()
                //values array is from the LiveData Util file
                //we the assert that the first value emitted is true
                assertEquals(dummyResults, values[0])
            }


        }



}