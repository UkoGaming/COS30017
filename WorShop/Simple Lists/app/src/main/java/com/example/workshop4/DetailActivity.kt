package com.example.workshop4

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val number = intent.getIntExtra("NUMBER", 0)
        findViewById<TextView>(R.id.numberDetailText).text = number.toString()

        val factText = findViewById<TextView>(R.id.factText)
        val isEven = number % 2 == 0
        val isDivisibleByThree = number % 3 == 0

        val fact = StringBuilder()
        fact.append(if (isEven) "It is an even number. " else "It is an odd number. ")
        if (isDivisibleByThree) {
            fact.append("It is divisible by three.")
        } else {
            fact.append("It is not divisible by three.")
        }

        factText.text = fact.toString()
    }
}