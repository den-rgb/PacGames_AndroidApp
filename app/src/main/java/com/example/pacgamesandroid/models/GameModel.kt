package com.example.pacgamesandroid.models;

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameModel(var id: Long = 0,
                     var title: String = "",
                     var price: String = "",
                     var genre: String = "",
                     var location: String = "",
                     var image: Uri = Uri.EMPTY) : Parcelable {
}

//@Parcelize
//data class Location(var lat: Double = 0.0,
//                    var lng: Double = 0.0,
//                    var zoom: Float = 0f) : Parcelable
