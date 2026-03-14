package com.example.assignment2

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

//Local Unit Test for GlobalState and Car logic.
//These tests run on the JVM (without an emulator)

class MainActivityTest {

    @Before
    fun setup() {
        // Reset state before each test
        GlobalState.creditBalance = 500
        GlobalState.cars.forEach { 
            it.isFavourite = false 
            it.isRented = false
            it.totalRentedDays = 0
        }
    }

    @Test
    fun testInitialCreditBalance() {
        assertEquals(500, GlobalState.creditBalance)
    }

    @Test
    fun testCarListCount() {
        // Based on GlobalState.kt, there should be 5 cars
        assertEquals(5, GlobalState.cars.size)
    }

    @Test
    fun testCarDataIntegrity() {
        val bmw = GlobalState.cars.find { it.name == "BMW" }
        assertTrue(bmw != null)
        assertEquals(2023, bmw?.year)
        assertEquals(80, bmw?.dailyRental)
    }

    @Test
    fun testFavouriteToggle() {
        val car = GlobalState.cars[0]
        assertFalse(car.isFavourite)
        
        car.isFavourite = true
        assertTrue(car.isFavourite)
        
        car.isFavourite = false
        assertFalse(car.isFavourite)
    }

    @Test
    fun testRentalLogicSimulation() {
        val car = GlobalState.cars[0] // BMW, dailyRental = 80
        val daysToRent = 3
        val expectedCost = car.dailyRental * daysToRent
        
        // Simulate renting
        GlobalState.creditBalance -= expectedCost
        car.isRented = true
        car.totalRentedDays = daysToRent
        
        assertEquals(500 - 240, GlobalState.creditBalance)
        assertTrue(car.isRented)
        assertEquals(3, car.totalRentedDays)
    }

    @Test
    fun testSearchLogicSimulation() {
        val query = "toyota"
        val filtered = GlobalState.cars.filter {
            it.name.lowercase().contains(query) || it.model.lowercase().contains(query)
        }
        
        assertEquals(1, filtered.size)
        assertEquals("Toyota", filtered[0].name)
    }
}
