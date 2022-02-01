package com.adyen.android.assignment.data.api.entity

import com.adyen.android.assignment.domain.model.VenueDetail

data class ResultsWrapper(
    val results: List<Results>?
)

fun ResultsWrapper.toVenueDetailsList():List<VenueDetail>?{
    return results?.map {
        VenueDetail(
            name = it.name,
            locationId = it.fsq_id
        )
    }
}