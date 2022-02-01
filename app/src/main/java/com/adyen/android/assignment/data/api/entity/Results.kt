package com.adyen.android.assignment.data.api.entity
data class Results(
    val categories: List<Category>,
    val chains: List<Any>,
    val distance: Int,
    val fsq_id: String,
    val geocodes: Geocodes,
    val location: Location,
    val name: String,
    val related_places: RelatedPlaces,
    val timezone: String
)


