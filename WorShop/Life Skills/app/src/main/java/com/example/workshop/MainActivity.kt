package com.example.workshop

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityLifecycle"
    private val KEY_RESULT = "calculation_result"
    private var calculationResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
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

        // Restore saved state if it exists
        if (savedInstanceState != null) {
            calculationResult = savedInstanceState.getString(KEY_RESULT)
            resultText.text = calculationResult
            Log.d(TAG, "Restored result: $calculationResult in onCreate")
        }

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
                
                calculationResult = result.toString()
                resultText.text = calculationResult
            } else {
                resultText.text = "Please enter both numbers"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called, saving: $calculationResult")
        outState.putString(KEY_RESULT, calculationResult)
    }
}