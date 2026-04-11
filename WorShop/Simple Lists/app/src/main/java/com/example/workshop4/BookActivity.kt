package com.example.workshop4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val books = listOf(
            Book("The Great Gatsby", "4.5/5", android.R.drawable.ic_menu_help),
            Book("1984", "4.8/5", android.R.drawable.ic_menu_help),
            Book("To Kill a Mockingbird", "4.9/5", android.R.drawable.ic_menu_help),
            Book("The Hobbit", "4.7/5", android.R.drawable.ic_menu_help),
            Book("Fahrenheit 451", "4.3/5", android.R.drawable.ic_menu_help),
            Book("Brave New World", "4.2/5", android.R.drawable.ic_menu_help),
            Book("Moby Dick", "3.9/5", android.R.drawable.ic_menu_help),
            Book("Pride and Prejudice", "4.6/5", android.R.drawable.ic_menu_help),
            Book("War and Peace", "4.1/5", android.R.drawable.ic_menu_help),
            Book("The Catcher in the Rye", "4.0/5", android.R.drawable.ic_menu_help)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.bookRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookAdapter(books)
    }
}