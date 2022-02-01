package com.adyen.android.assignment.data

import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.data.api.entity.ResultsWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val placesService: PlacesService) {
    suspend fun getVenueRecommendations(query: Map<String, String>):Flow<Response<ResultsWrapper>>{
        return   flow {
            emit(placesService.getVenueRecommendations(query))
        }
    }

}