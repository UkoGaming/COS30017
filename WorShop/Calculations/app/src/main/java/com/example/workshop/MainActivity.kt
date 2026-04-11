package com.example.workshop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val num1Edit = findViewById<EditText>(R.id.number1)
        val num2Edit = findViewById<EditText>(R.id.number2)
        val calculateBtn = findViewById<Button>(R.id.calculate_button)
        val resultText = findViewById<TextView>(R.id.result_text)
        val operationGroup = findViewById<RadioGroup>(R.id.operation_group)

        // The line below sets up the listener for the button
        calculateBtn.setOnClickListener {
            val num1Str = num1Edit.text.toString()
            val num2Str = num2Edit.text.toString()

            if (num1Str.isNotEmpty() && num2Str.isNotEmpty()) {
                val num1 = num1Str.toInt()
                val num2 = num2Str.toInt()
                
                val result = when (operationGroup.checkedRadioButtonId) {
                    R.id.radio_add -> num1 + num2
                    R.id.radio_subtract -> num1 - num2
                    R.id.radio_multiply -> num1 * num2
                    else -> num1 + num2
                }
                
                resultText.text = result.toString()
            } else {
                resultText.text = "Please enter both numbers"
            }
        }
    }
}