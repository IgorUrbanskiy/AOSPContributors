package com.example.aospcontributors.presentation.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aospcontributors.App
import com.example.aospcontributors.domain.repositories.MapRepository
import com.example.aospcontributors.presentation.Resource
import com.mapbox.geojson.Point

/**
 * Created by Ihor Urbanskyi on 31.03.2019.
 */

class MapViewModel : ViewModel() {

    val locationLiveData = MutableLiveData<Resource<Point>>()

    private var contributorsRepository: MapRepository =
        App.instance.componentFactory.getMapRepository()

    fun convertAddressToLocation(address: String) {
        contributorsRepository.getLocationByAddress(address, ::onSuccess, ::onError)
    }

    private fun onSuccess(point: Point) {
        locationLiveData.postValue(Resource.success(point))
    }

    private fun onError(t: Throwable) {
        Log.e(com.example.aospcontributors.presentation.contributors.TAG, t.message)
        locationLiveData.postValue(Resource.error(msg = t.message!!, data = null))
    }
}