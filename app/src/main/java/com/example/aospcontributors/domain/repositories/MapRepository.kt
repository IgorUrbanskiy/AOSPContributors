package com.example.aospcontributors.domain.repositories

import com.mapbox.geojson.Point

/**
 * Created by Ihor Urbanskyi on 31.03.2019.
 */
interface MapRepository {

    fun getLocationByAddress(address: String, onSuccess: (Point) -> Unit, onError: (Throwable) -> Unit)
}