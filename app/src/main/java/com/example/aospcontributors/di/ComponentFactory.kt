package com.example.aospcontributors.di

import com.example.aospcontributors.domain.repositories.ContributorsRepository
import com.example.aospcontributors.domain.repositories.MapRepository

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
interface ComponentFactory {

    fun getContributorRepository(): ContributorsRepository

    fun getMapRepository(): MapRepository
}