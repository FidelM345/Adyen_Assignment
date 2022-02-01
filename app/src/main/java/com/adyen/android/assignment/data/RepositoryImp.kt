package com.adyen.android.assignment.data

import com.adyen.android.assignment.data.api.entity.ResultsWrapper
import com.adyen.android.assignment.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class RepositoryImp @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    Repository {

    override suspend fun getVenueRecommendations(query: Map<String, String>): Flow<Response<ResultsWrapper>> {

        return remoteDataSource.getVenueRecommendations(query)

    }

}