package com.example.workshop3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiplicationData(
    val factor1: Int,
    val factor2: Int
) : Parcelable {
    val product: Int
        get() = factor1 * factor2
}