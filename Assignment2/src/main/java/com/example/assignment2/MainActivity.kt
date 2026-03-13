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
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.AdapterView

//Home Screen
class MainActivity : AppCompatActivity() {
    lateinit var ArrowLeftBtn : ImageView
    lateinit var ArrowRightBtn : ImageView
    lateinit var IconFavourite : ImageView
    lateinit var CarImage : ImageView
    lateinit var RentBtn : Button
    lateinit var FavouriteBtn : Button
    lateinit var DetailsBtn: Button
    lateinit var DarModeSwh : Switch
    lateinit var ratingBar : RatingBar
    lateinit var DetailRight : TextView
    lateinit var CurrentCredit : TextView
    lateinit var SearchBox : SearchView
    lateinit var SortMenu : Spinner
    var currentIndex = 0
    var darkMode = false
    var displayedCars = mutableListOf<Car>()
    fun showCar() {

        if (displayedCars.isEmpty()) return

        val car = displayedCars[currentIndex]

        if (!car.isRented) {
            DetailRight.text =
                "${car.name}\n${car.model}\n${car.year}\n${car.kilometres}\n${car.dailyRental}"

            ratingBar.rating = car.rating

            CarImage.setImageResource(car.imageRes)

            if (car.isFavourite) {
                IconFavourite.setColorFilter(
                    ContextCompat.getColor(this, R.color.isfavourtire)
                )
            } else {
                IconFavourite.clearColorFilter()
            }
        }
    }
    fun updateCredit() {
        CurrentCredit.text = getString(R.string.credit, GlobalState.creditBalance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Image View
        ArrowLeftBtn = findViewById(R.id.arrowLeft)
        ArrowRightBtn = findViewById(R.id.arrowRight)
        IconFavourite = findViewById(R.id.icon_favourite)
        CarImage = findViewById(R.id.carImage)

        //Button
        RentBtn = findViewById(R.id.button_rent)
        FavouriteBtn = findViewById(R.id.button_favourite)
        DetailsBtn = findViewById(R.id.button_info)

        //Switch
        DarModeSwh = findViewById(R.id.switch_darkmode)

        //Rating Bar
        ratingBar = findViewById(R.id.ratingBar)

        //Text View
        DetailRight = findViewById(R.id.detailRight)
        CurrentCredit = findViewById(R.id.credit)

        //Search Bar
        SearchBox = findViewById(R.id.searchBox)

        //Drop Down
        SortMenu = findViewById(R.id.sortMenu)

        displayedCars = GlobalState.cars.toMutableList()

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.sort_menu,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SortMenu.adapter = adapter

        if (savedInstanceState != null) {
            GlobalState.creditBalance = savedInstanceState.getInt("creditBalance", 500)
            currentIndex = savedInstanceState.getInt("currentIndex", 0)

            darkMode = savedInstanceState.getBoolean("darkMode", false)

            GlobalState.cars.forEachIndexed { index, car ->
                car.isFavourite = savedInstanceState.getBoolean("fav_$index", false)
            }
        }

        showCar()
        updateCredit()
        DarModeSwh.isChecked = darkMode

        //All buttons
        //Next button
        ArrowRightBtn.setOnClickListener {
            currentIndex = (currentIndex + 1) % displayedCars.size
            showCar()
            Log.d("Checking", "Car Changed Forward")
        }

        //Previous button
        ArrowLeftBtn.setOnClickListener {
            currentIndex = (currentIndex - 1 + displayedCars.size) % displayedCars.size
            showCar()
            Log.d("Checking", "Car Changed Forward")
        }

        //Rent button
        RentBtn.setOnClickListener {
            val intent = Intent(this, RentingActivity::class.java)
            val car = displayedCars[currentIndex]

            intent.putExtra("carIndex", GlobalState.cars.indexOf(car))

            startActivity(intent)
        }

        //Favourite Page
        FavouriteBtn.setOnClickListener {
            val intent = Intent(this, Favourite_Activity::class.java)
            startActivity(intent)
        }

        //Detail Page
        DetailsBtn.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        //Darkmode Switch
        DarModeSwh.setOnCheckedChangeListener { _, isChecked ->

            darkMode = isChecked

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        //Favourite Icon
        IconFavourite.setOnClickListener {

            val car = displayedCars[currentIndex]

            car.isFavourite = !car.isFavourite

            showCar()

            Log.d("Checking", "Favourite toggled: ${car.name}")
        }

        //Search Bar
        SearchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val query = newText?.lowercase() ?: ""

                displayedCars = GlobalState.cars.filter {
                    it.name.lowercase().contains(query) ||
                            it.model.lowercase().contains(query)
                }.toMutableList()

                currentIndex = 0

                if (displayedCars.isNotEmpty()) {
                    showCar()
                }

                return true
            }
        })

        //Sorting
        SortMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                when (position) {

                    0 -> { // Rating high → low
                        displayedCars.sortByDescending { it.rating }
                    }

                    1 -> { // Year newest → oldest
                        displayedCars.sortByDescending { it.year }
                    }

                    2 -> { // Cost low → high
                        displayedCars.sortBy { it.dailyRental }
                    }
                }

                currentIndex = 0
                showCar()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onResume() {
        super.onResume()

        displayedCars = GlobalState.cars.filter { !it.isRented }.toMutableList()

        if (currentIndex >= displayedCars.size) {
            currentIndex = 0
        }

        updateCredit()
        showCar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("creditBalance", GlobalState.creditBalance)
        outState.putInt("currentIndex", currentIndex)
        outState.putBoolean("darkMode", DarModeSwh.isChecked)

        GlobalState.cars.forEachIndexed { index, car ->
            outState.putBoolean("fav_$index", car.isFavourite)

        }
    }
}