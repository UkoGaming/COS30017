package com.example.assignment2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Favourite_Activity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.favourite)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewFavourite)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.lsCars)
        backBtn = findViewById(R.id.button_back)

        val favouriteCars = GlobalState.cars
            .filter { it.isFavourite }
            .toMutableList()

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = FavouriteReViewAdapt(favouriteCars)

        backBtn.setOnClickListener {
            finish()
            Log.d("Checking", "Back to Main Page")
        }
    }
}