package com.example.aospcontributors.presentation.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.aospcontributors.R
import com.example.aospcontributors.presentation.Resource
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

private const val TAG = "MapActivity"

const val EXTRA_ADDRESS = "EXTRA_ADDRESS"

class MapActivity : AppCompatActivity() {

    private var mapView: MapView? = null

    private lateinit var mapBoxMap: MapboxMap

    private lateinit var style: Style

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.aospcontributors.R.layout.activity_map)
        mapView = findViewById(com.example.aospcontributors.R.id.map_view)
        mapView?.onCreate(savedInstanceState)

        val mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        mapViewModel.locationLiveData.observe(this, Observer {
            handleLocation(it)
        })
        val location = intent.getStringExtra(EXTRA_ADDRESS)
        title = location
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapView?.getMapAsync { mapboxMap ->
            this.mapBoxMap = mapboxMap
            mapViewModel.convertAddressToLocation(location)
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) {
                style = it
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun handleLocation(resource: Resource<Point>) {
        Log.d(TAG, "handleLocation " + resource.data)
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                resource.data?.let {
                    showUserLocation(it)
                }
            }
            Resource.Status.ERROR -> {
                showError()
            }
        }
    }

    private fun showUserLocation(point: Point) {
        style.addImage(
            "marker-icon-id",
            BitmapFactory.decodeResource(
                resources,
                com.example.aospcontributors.R.drawable.mapbox_marker_icon_default
            )
        )

        val geoJsonSource = GeoJsonSource(
            "source-id", Feature.fromGeometry(
                Point.fromLngLat(point.longitude(), point.latitude())
            )
        )
        style.addSource(geoJsonSource)

        val symbolLayer = SymbolLayer("layer-id", "source-id")
        symbolLayer.withProperties(
            PropertyFactory.iconImage("marker-icon-id")
        )
        style.addLayer(symbolLayer)

        moveCamera(point)
    }

    private fun moveCamera(point: Point) {
        val position = CameraPosition.Builder()
            .target(LatLng(point.latitude(), point.longitude()))
            .zoom(12.0)
            .tilt(20.0)
            .build()
        mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000)
    }

    private fun showError() {
        Toast.makeText(this, R.string.error_location_retreiving, Toast.LENGTH_LONG).show()
    }

    public override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
