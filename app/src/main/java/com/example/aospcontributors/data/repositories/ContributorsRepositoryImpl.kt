package com.example.aospcontributors.data.repositories

import android.util.Log
import com.example.aospcontributors.data.network.ApiService
import com.example.aospcontributors.data.network.entities.Contributor
import com.example.aospcontributors.data.network.entities.User
import com.example.aospcontributors.domain.repositories.ContributorsRepository
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
private const val TAG: String = "ContributorsRepository"

class ContributorsRepositoryImpl(private val apiService: ApiService) : ContributorsRepository {

    override fun getContributors(onSuccess: (List<Contributor>) -> Unit, onError: (Throwable) -> Unit) {
        apiService.getContributors()
            .enqueue(object : Callback<List<Contributor>> {
                override fun onResponse(call: Call<List<Contributor>>, response: Response<List<Contributor>>) {
                    if (response.isSuccessful) {
                        val contributors = response.body()
                        Log.d(TAG, contributors?.size.toString())
                        contributors?.let {
                            onSuccess(it)
                        }
                    } else {
                        onError(Throwable(response.message()))
                    }
                }

                override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                    Log.e(TAG, t.message)
                    onError(t)
                }
            })
    }

    override fun getUser(userName: String): Observable<User> {
        return apiService.getUser(userName)
    }
}