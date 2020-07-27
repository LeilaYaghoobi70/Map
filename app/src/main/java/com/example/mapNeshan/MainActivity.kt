package com.example.mapNeshan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mapNeshan.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.neshan.core.LngLat
import org.neshan.core.Range
import org.neshan.layers.VectorElementLayer
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices
import org.neshan.styles.AnimationStyleBuilder
import org.neshan.styles.AnimationType
import org.neshan.styles.MarkerStyleCreator
import org.neshan.utils.BitmapUtils
import org.neshan.vectorelements.Marker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var markerLayer: VectorElementLayer
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1000
    private lateinit var location: Location
    private lateinit var marker: Marker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()
        initMap()
        binding.fab.setOnClickListener({
            getLastLocation()
        })
    }

    private fun initMap() {
        markerLayer = NeshanServices.createVectorElementLayer()
        binding.map.layers.add(markerLayer)
        binding.map.setFocalPointPosition(LngLat(53.529929, 35.164676), 1f)
        binding.map.setZoom(4.5f, 1f)
        binding.map.options.setZoomRange(Range(4.5f, 18f))
        binding.map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    onLocationChange(task.result)
                } else {
                    Toast.makeText(this, "موقعیت یافت نشد.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun LngLat.focus() {
        binding.map.setZoom(17f, 1f)
        binding.map.setFocalPointPosition(
            this,
            1f
        )
    }

    private fun onLocationChange(location: Location) {
        val loc: LngLat = LngLat(location.longitude, location.latitude)
        loc.focus()
        setMarker(loc)
    }

    private fun setMarker(markerLocation: LngLat) {
        markerLayer.clear()

        val animSt = AnimationStyleBuilder()
            .let {
                it.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
                it.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
                it.phaseInDuration = 0.5f
                it.phaseOutDuration = 0.5f
                it.buildStyle()
            }

        val st = MarkerStyleCreator()
            .let {
                it.size = 48f
               /* val drawable: Drawable? =
                    ContextCompat.getDrawable(baseContext, R.drawable.ic_baseline_location_on_24)
                val bitmap = drawable as BitmapFactory
                it.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_location_on_24)
                )*/
                it.animationStyle = animSt
                it.buildStyle()
            }

        marker = Marker(markerLocation, st)
        markerLayer.add(marker)
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