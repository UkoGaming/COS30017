package com.example.workshop3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var score = 0
    private lateinit var currentData: MultiplicationData
    private lateinit var tvScore: TextView
    private lateinit var tvQuestion: TextView

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val isCorrect = result.data?.getBooleanExtra("is_correct", false) ?: false
            if (isCorrect) {
                score++
                updateScoreDisplay()
            }
            generateNewQuestion()
        }
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

        tvScore = findViewById(R.id.tvScore)
        tvQuestion = findViewById(R.id.tvQuestion)
        val btnShowAnswer = findViewById<Button>(R.id.btnShowAnswer)

        generateNewQuestion()
        updateScoreDisplay()

        btnShowAnswer.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("multiplication_data", currentData)
            }
            getResult.launch(intent)
        }
    }

    private fun generateNewQuestion() {
        val factor1 = Random.nextInt(1, 13)
        val factor2 = Random.nextInt(1, 13)
        currentData = MultiplicationData(factor1, factor2)
        tvQuestion.text = "${currentData.factor1} x ${currentData.factor2} = ?"
    }

    private fun updateScoreDisplay() {
        tvScore.text = "Score: $score"
    }
}