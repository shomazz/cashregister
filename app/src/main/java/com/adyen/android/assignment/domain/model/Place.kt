package com.adyen.android.assignment.domain.model

data class Place(
    val id: String?,
    val name: String?,
    val distance: Int?,
    val address: String?,
    val categories: List<Category>?,
)