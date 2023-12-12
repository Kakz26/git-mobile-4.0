package com.example.finaldicoding

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    val name: String,
    val description: String,
    val photo: Int,
    val sound: String,
    var isBookmark: Boolean = false,
) : Parcelable
