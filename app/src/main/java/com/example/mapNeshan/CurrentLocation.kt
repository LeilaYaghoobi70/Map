package com.example.mapNeshan

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager


class CurrentLocation constructor(val context: Context) {

    private var lat: Double = 0.0
    private var lng: Double = 0.0
    protected var locationManager: LocationManager? = null
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var canGetLocation = false

   /* fun getLocation(): Location {
        locationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }*/
}