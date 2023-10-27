package com.haki.storyapp.ui

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.haki.storyapp.R
import com.haki.storyapp.databinding.ActivityMapsBinding
import com.haki.storyapp.repo.ResultState
import com.haki.storyapp.response.ListStoryItem
import com.haki.storyapp.ui.viewModel.MapsViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var listLoc: List<ListStoryItem>
    private val boundsBuilder = LatLngBounds.Builder()

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnInfoWindowClickListener {
            val toDetail = Intent(this@MapsActivity, DetailActivity::class.java)
            toDetail.putExtra(DetailActivity.ID_STORY, it.tag.toString())
            startActivity(toDetail)
        }

        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 5f))
            true
        }

        setMapStyle()
        getStories()
    }

    private fun getStories() {
        viewModel.stories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Success -> {
                        listLoc = result.data.listStory
                        addMarkers()
                    }

                    is ResultState.Error -> {

                    }

                    else -> {
                        //Do Nothing
                    }
                }

            }

        }
    }

    private fun addMarkers() {
        mMap.clear()
        listLoc.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            val marker = mMap.addMarker(
                MarkerOptions().position(latLng).title(story.name + getString(R.string.forDetail))
                    .snippet(story.description)
            )
            boundsBuilder.include(latLng)
            marker?.tag = story.id
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}