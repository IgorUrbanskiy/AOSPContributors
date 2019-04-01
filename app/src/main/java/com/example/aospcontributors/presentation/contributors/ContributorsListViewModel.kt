package com.example.aospcontributors.presentation.contributors

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aospcontributors.App
import com.example.aospcontributors.data.network.entities.Contributor
import com.example.aospcontributors.data.network.entities.User
import com.example.aospcontributors.domain.repositories.ContributorsRepository
import com.example.aospcontributors.presentation.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */

const val TAG = "ContributorsListViewMod"

const val CONTRIBUTORS_COUNT = 5

class ContributorsListViewModel : ViewModel() {

    val contributorsLiveData = MutableLiveData<Resource<List<ContributorItem>>>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var contributorsRepository: ContributorsRepository =
            App.instance.componentFactory.getContributorRepository()

    init {
        getContributors()
    }

    private fun getContributors() {
        contributorsLiveData.value = Resource.loading()
        contributorsRepository.getContributors(::onSuccess, ::onError)
    }

    private fun onSuccess(contributors: List<Contributor>) {
        val result = contributors
                .sortedWith(compareByDescending { it.contributions })
                .take(CONTRIBUTORS_COUNT)

        compositeDisposable.add(Observable
                .fromIterable(result)
                .flatMap { contributor: Contributor ->
                    contributorsRepository.getUser(contributor.login)
                            .map { user: User ->
                                Log.d(TAG, "user: $user")
                                Log.d(TAG, "contributor: $contributor")
                                ContributorItem(
                                        login = contributor.login,
                                        id = contributor.id,
                                        avatarUrl = contributor.avatarUrl,
                                        contributions = contributor.contributions,
                                        location = user.location
                                )
                            }
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contributors ->
                    Log.d(TAG, "contributors: $contributors")
                    contributorsLiveData.postValue(Resource.success(contributors))
                }, { throwable -> Log.d(TAG, "contributors load failed: $throwable") })
        )
    }

    private fun onError(t: Throwable) {
        Log.e(TAG, t.message)
        contributorsLiveData.postValue(Resource.error(msg = t.message!!, data = null))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}