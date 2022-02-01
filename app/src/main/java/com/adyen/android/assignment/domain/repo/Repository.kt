package com.adyen.android.assignment.domain.repo

import com.adyen.android.assignment.data.api.entity.ResultsWrapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {
   suspend fun getVenueRecommendations(query: Map<String, String>): Flow<Response<ResultsWrapper>>
}