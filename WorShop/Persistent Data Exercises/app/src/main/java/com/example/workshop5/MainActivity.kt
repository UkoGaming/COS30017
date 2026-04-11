package com.example.workshop5

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataList = mutableListOf<LineData>()

        // Reading from res/raw/data.txt
        // Access files in res/raw using resources.openRawResource()
        resources.openRawResource(R.raw.data).bufferedReader().forEachLine { line ->
            // Log each line to Logcat
            Log.d("FileRead", line)

            // Split and create objects for RecyclerView
            val parts = line.split(",")
            if (parts.size == 2) {
                dataList.add(LineData(parts[0], parts[1].trim()))
            }
        }

        // Setting up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DataAdapter(dataList)
    }
}
