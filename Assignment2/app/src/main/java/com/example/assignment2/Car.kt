package com.example.assignment2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(

    val name: String,
    val model: String,
    val year: Int,
    val rating: Float,
    val kilometres: Int,
    val dailyRental: Int,
    val imageRes: Int,
    var totalRentedDays: Int,
    var isFavourite: Boolean = false,
    var isRented: Boolean = false

): Parcelable
