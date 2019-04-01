package com.example.aospcontributors.data.repositories

import android.util.Log
import com.example.aospcontributors.BuildConfig
import com.example.aospcontributors.domain.repositories.MapRepository
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ihor Urbanskyi on 31.03.2019.
 */

private const val TAG = "MapRepositoryImpl"

class MapRepositoryImpl : MapRepository {

    override fun getLocationByAddress(address: String, onSuccess: (Point) -> Unit, onError: (Throwable) -> Unit) {
        val mapboxGeocoding = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
            .query(address)
            .build()
        mapboxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                val results = response.body()!!.features()
                if (results.size > 0) {
                    val firstResultPoint = results[0].center()
                    Log.d(TAG, "onResponse: " + firstResultPoint!!.toString())
                    onSuccess(firstResultPoint)
                } else {
                    onError(Throwable("No result found"))
                    Log.d(TAG, "onResponse: No result found")
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                Log.d(TAG, "onFailure $throwable")
                onError(throwable)
            }
        })
    }
}