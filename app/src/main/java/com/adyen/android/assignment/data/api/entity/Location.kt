package com.adyen.android.assignment.data.api.entity

data class Location(
    val address: String,
    val country: String,
    val cross_street: String,
    val locality: String,
    val neighborhood: List<String>,
    val postcode: String,
    val region: String
)