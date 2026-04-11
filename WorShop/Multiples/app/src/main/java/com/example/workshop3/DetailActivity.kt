package com.example.workshop3

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("multiplication_data", MultiplicationData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("multiplication_data")
        }

        val tvAnswer = findViewById<TextView>(R.id.tvAnswer)
        tvAnswer.text = data?.product?.toString() ?: "Error"

        val btnCorrect = findViewById<Button>(R.id.btnCorrect)
        val btnIncorrect = findViewById<Button>(R.id.btnIncorrect)

        btnCorrect.setOnClickListener {
            setResultAndFinish(true)
        }

        btnIncorrect.setOnClickListener {
            setResultAndFinish(false)
        }
    }

    private fun setResultAndFinish(isCorrect: Boolean) {
        val resultIntent = Intent().apply {
            putExtra("is_correct", isCorrect)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}