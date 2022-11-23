package com.example.pacgamesandroid.models;

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var image: Uri = Uri.EMPTY) : Parcelable


