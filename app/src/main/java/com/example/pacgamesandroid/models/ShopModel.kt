package com.example.pacgamesandroid.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopModel(
    var id: Int = 0,
    var title: String = "",
    var games: MutableList<GameModel> = mutableListOf(),
    var coordinates: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable