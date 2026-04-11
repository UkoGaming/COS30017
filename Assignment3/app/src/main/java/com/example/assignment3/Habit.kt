package com.example.assignment3

import java.io.Serializable
import java.util.Date
import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String = "",
    var icon: String = "⭐",
    var streak: Int = 0,
    var isDone: Boolean = false,
    val startDate: Date = Date(),
    var completedDates: Set<Long> = emptySet(),
    var isProgressHabit: Boolean = false,
    var isManualProgress: Boolean = false, // True for Sleep, Water, etc.
    var targetValue: Int = 5000,
    var currentProgress: Int = 0,
    var unit: String = "steps" // "steps", "hours", "kcal", "ml"
) : Serializable {
    val lastCompletedDate: Long?
        get() = completedDates.maxOrNull()
}