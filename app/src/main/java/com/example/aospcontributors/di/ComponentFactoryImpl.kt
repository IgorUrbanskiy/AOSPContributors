package com.example.aospcontributors.di

import com.example.aospcontributors.data.network.ApiService
import com.example.aospcontributors.data.repositories.ContributorsRepositoryImpl
import com.example.aospcontributors.data.repositories.MapRepositoryImpl
import com.example.aospcontributors.domain.repositories.ContributorsRepository
import com.example.aospcontributors.domain.repositories.MapRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
class ComponentFactoryImpl : ComponentFactory {

    companion object {
        const val BASE_URL: String = "https://api.github.com"
    }

    override fun getContributorRepository(): ContributorsRepository {
        return ContributorsRepositoryImpl(getApiService())
    }

    override fun getMapRepository(): MapRepository {
        return MapRepositoryImpl()
    }

    private fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}