package com.example.aospcontributors.domain.repositories

import com.example.aospcontributors.data.network.entities.Contributor
import com.example.aospcontributors.data.network.entities.User
import io.reactivex.Observable

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
interface ContributorsRepository {
    fun getContributors(onSuccess: (List<Contributor>) -> Unit, onError: (Throwable) -> Unit = {})

    fun getUser(userName: String): Observable<User>
}