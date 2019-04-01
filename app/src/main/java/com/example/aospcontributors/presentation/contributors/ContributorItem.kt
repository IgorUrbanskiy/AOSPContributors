package com.example.aospcontributors.presentation.contributors

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */
data class ContributorItem(
    var login: String,
    var id: Int,
    var avatarUrl: String,
    var contributions: Int,
    var location: String?
)