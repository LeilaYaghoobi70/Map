package com.example.mapNeshan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mapNeshan.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.neshan.core.Bounds
import org.neshan.core.LngLat
import org.neshan.core.Range
import org.neshan.layers.VectorElementLayer
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var makerLayer: VectorElementLayer
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val BASE_MAP_INDEX = 0
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()
        initMap()
    }

    private fun initMap() {
        makerLayer = NeshanServices.createVectorElementLayer()
        binding.map.layers.add(makerLayer)
        binding.map.setFocalPointPosition(LngLat(53.529929, 35.164676), 1f)
        binding.map.setZoom(4.5f, 1f)
        binding.map.options.setZoomRange(Range(4.5f, 18f))
        binding.map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        //setMapBounds()
    }

    private fun setMapBounds() {
        val bounds = Bounds(
            LngLat(43.505859, 24.647017),
            LngLat(63.984375, 40.178873)
        )
        binding.map.options.setPanBounds(bounds)
    }


    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE && checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
                finish()
        }

    }
}