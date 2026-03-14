package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class RentingActivity : AppCompatActivity() {

    lateinit var carImage : ImageView
    lateinit var detailRight : TextView
    lateinit var ratingBar : RatingBar
    lateinit var credit : TextView
    lateinit var dayofRental : TextView
    lateinit var totalCost : TextView
    lateinit var backBtn : Button
    lateinit var confirmBtn : Button
    lateinit var daySet : SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.renting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rent)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carImage = findViewById(R.id.carImage)
        detailRight = findViewById(R.id.detailRight)
        ratingBar = findViewById(R.id.ratingBar)
        credit = findViewById(R.id.credit)
        backBtn = findViewById(R.id.button_back)
        confirmBtn = findViewById(R.id.confirm_renting)
        daySet = findViewById(R.id.daySet)
        dayofRental = findViewById(R.id.daysOfRental)
        totalCost = findViewById(R.id.totalcost)

        val carIndex = intent.getIntExtra("carIndex", 0)

        val car = GlobalState.cars[carIndex]

        carImage.setImageResource(car.imageRes)

        ratingBar.rating = car.rating

        detailRight.text =
            "${car.name}\n${car.model}\n${car.year}\n${car.kilometres}\n${car.dailyRental}"

        credit.text = getString(R.string.credit, GlobalState.creditBalance)

        dayofRental.text = getString(R.string.dayOfRental, 1)
        totalCost.text = getString(R.string.totalCost, car.dailyRental)

        backBtn.setOnClickListener {
            finish()
            Log.d("Checking", "Back to Main Page")
        }

        daySet.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val days = progress + 1
                val totalPrice = car.dailyRental * days

                dayofRental.text = getString(R.string.dayOfRental, days)
                totalCost.text = getString(R.string.totalCost, totalPrice)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        confirmBtn.setOnClickListener {

            val days = daySet.progress + 1
            val totalPrice = car.dailyRental * days

            confirmBtn.isEnabled = GlobalState.creditBalance >= totalPrice

            if (totalPrice <= 400) {

                if (GlobalState.creditBalance >= totalPrice) {

                    GlobalState.creditBalance -= totalPrice

                    car.isRented = true

                    car.totalRentedDays = days

                    Toast.makeText(this, "Car rented for $days days!", Toast.LENGTH_SHORT).show()

                    Log.d("Checking", "Car Rented")

                    finish()

                    Log.d("Checking", "Back to Main Page")

                } else {

                    Toast.makeText(this, "Not enough credit!", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Purchase cannot exceed 400!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}