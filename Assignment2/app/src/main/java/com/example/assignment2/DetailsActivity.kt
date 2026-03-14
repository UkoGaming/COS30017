package com.example.assignment2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewRent)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.lsCars)
        backBtn = findViewById(R.id.button_back)

        val rentedCars = GlobalState.cars
            .filter { it.isRented }
            .toMutableList()

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = DetailsReViewAdapt(rentedCars)

        backBtn.setOnClickListener {
            finish()
            Log.d("Checking", "Back to Main Page")
        }
    }
}