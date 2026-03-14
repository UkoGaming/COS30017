package com.example.assignment2

object GlobalState {
    var creditBalance: Int = 500

    val cars = mutableListOf(
        Car("BMW", "430i", 2023, 4.5f, 12000, 80, R.drawable.bmw, 0),
        Car("Lexus", "ES250", 2016, 3.5f, 15000, 75, R.drawable.lexus, 0),
        Car("Toyota", "Alphard", 2022, 4.0f, 18000, 70, R.drawable.toyota, 0),
        Car("Volvo", "S90L Ultimate", 2022, 5.0f, 9000, 90, R.drawable.volvo, 0),
        Car("Volkswagen", "Viloran Luxury", 2023, 4.5f, 11000, 85, R.drawable.volkswagen, 0)
    )
}