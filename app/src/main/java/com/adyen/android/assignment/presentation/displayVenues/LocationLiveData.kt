package com.adyen.android.assignment.presentation.displayVenues

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.adyen.android.assignment.core.Constants.FASTEST_LOCATION_UPDATE_AFTER_40_SECONDS
import com.adyen.android.assignment.core.Constants.LOCATION_UPDATE_AFTER_1_MINUTE
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class LocationLiveData @Inject constructor(context: Context) : LiveData<Location>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Dispatches values to active observers
     *
     */
    private fun setLocationData(location: Location) {
        value = location
    }

    /**
     * Static object of location request
     */
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create()
            .apply {
                interval = LOCATION_UPDATE_AFTER_1_MINUTE
                fastestInterval = FASTEST_LOCATION_UPDATE_AFTER_40_SECONDS
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
    }


    /**
     * called when there are no active observers
     */
    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    /**
     * Called when there are active observers
     */
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    /**
     * Callback that triggers on location updates available
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    /**
     * Initiate Location Updates using Fused Location Provider and
     * attaching callback to listen location updates
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

}
