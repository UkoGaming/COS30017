package com.example.assignment3

data class HabitSuggestion(
    val name: String,
    val emoji: String,
    val isProgress: Boolean = false,
    val isManual: Boolean = false,
    val target: Int = 1,
    val unit: String = "steps"
)