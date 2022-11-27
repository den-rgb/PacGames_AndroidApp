package com.example.pacgamesandroid.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopModel(
    var id: Long = 0,
    var title: String = "",
    var games: List<GameModel>? = null,
    var coordinates: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable