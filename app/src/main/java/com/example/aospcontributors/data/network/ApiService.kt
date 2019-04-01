package com.example.aospcontributors.data.network

import com.example.aospcontributors.data.network.entities.Contributor
import com.example.aospcontributors.data.network.entities.User
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
interface ApiService {

    @GET("repos/aosp-mirror/platform_development/contributors")
    fun getContributors(): Call<List<Contributor>>

    @GET("users/{username}")
    fun getUser(@Path("username") userName: String): Observable<User>
}