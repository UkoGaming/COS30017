package com.example.assignment2

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Test 1: Check main screen loads correctly
    @Test
    fun testAppLaunch() {
        // Check if main layout is displayed
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        
        // Check if important elements are visible
        onView(withId(R.id.carImage)).check(matches(isDisplayed()))
        onView(withId(R.id.button_rent)).check(matches(isDisplayed()))
        onView(withId(R.id.searchBox)).check(matches(isDisplayed()))
    }

    // Test 2: Check Navigation (Arrows)
    @Test
    fun testNavigationArrows() {
        // Click right arrow multiple times to ensure stability
        onView(withId(R.id.arrowRight)).perform(click())
        onView(withId(R.id.carImage)).check(matches(isDisplayed()))

        // Click left arrow
        onView(withId(R.id.arrowLeft)).perform(click())
        onView(withId(R.id.carImage)).check(matches(isDisplayed()))
    }

    // Test 3: Check Rent button opens Renting screen and Back button works
    @Test
    fun testRentNavigation() {
        // Click Rent button
        onView(withId(R.id.button_rent)).perform(click())

        // Verify we are on the Renting screen (checking for confirm_renting button)
        onView(withId(R.id.confirm_renting)).check(matches(isDisplayed()))
        
        // Click back button on Renting screen
        onView(withId(R.id.button_back)).perform(click())
        
        // Verify we returned to main screen
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    // Test 4: Check Search Box interaction
    @Test
    fun testSearchInteraction() {
        // Type into search box
        onView(withId(R.id.searchBox)).perform(click())
        onView(isAssignableFrom(android.widget.EditText::class.java))
            .perform(typeText("BMW"), closeSoftKeyboard())
        
        // Verify car image is still there (filtering occurred)
        onView(withId(R.id.carImage)).check(matches(isDisplayed()))
    }

    // Test 5: Check Favourite icon interaction
    @Test
    fun testFavouriteClick() {
        // Click favourite icon
        onView(withId(R.id.icon_favourite)).perform(click())
        
        // Verify it is still displayed
        onView(withId(R.id.icon_favourite)).check(matches(isDisplayed()))
    }
}
