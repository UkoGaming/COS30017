package com.example.workshop4

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NumberAdapter
    private var currentList = (1..20).toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = NumberAdapter(currentList) { number ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("NUMBER", number)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.length_10 -> {
                currentList = (1..10).toList()
                adapter.updateData(currentList)
                true
            }
            R.id.length_50 -> {
                currentList = (1..50).toList()
                adapter.updateData(currentList)
                true
            }
            R.id.show_books -> {
                startActivity(Intent(this, BookActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}