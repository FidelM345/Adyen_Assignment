package com.adyen.android.assignment.domain.usecase

import com.adyen.android.assignment.domain.repo.Repository
import javax.inject.Inject

class GetVenueDetailsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(query: Map<String,String>)= repository.getVenueRecommendations(query)

}