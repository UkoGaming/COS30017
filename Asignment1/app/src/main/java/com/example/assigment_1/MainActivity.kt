package com.example.assigment_1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnAdd: Button
    lateinit var btnSub: Button
    lateinit var btnReset: Button
    lateinit var txtSum: TextView
    lateinit var txtEle: TextView
    lateinit var btnEnglish: Button
    lateinit var btnVietnamese: Button
    var deducted = false
    var scoreColor = Color.BLUE
    var total = 0
    var elementNum = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnAdd = findViewById(R.id.button_perform)
        btnSub = findViewById(R.id.button_deduction)
        btnReset = findViewById(R.id.button_reset)
        btnEnglish = findViewById(R.id.button_english)
        btnVietnamese = findViewById(R.id.button_vietnamese)

        txtSum = findViewById(R.id.sum)
        txtEle =findViewById(R.id.element)

        //Set onClickListeners for buttons
        btnAdd.setOnClickListener(this)
        btnSub.setOnClickListener(this)
        btnReset.setOnClickListener(this)

        btnEnglish.setOnClickListener { changeLanguage("en") }
        btnVietnamese.setOnClickListener { changeLanguage("vi") }

        //Restoring Instance after rotation
        if (savedInstanceState != null) {
            total = savedInstanceState.getInt("total")
            elementNum = savedInstanceState.getInt("element")
            deducted = savedInstanceState.getBoolean("deducted")
            scoreColor = savedInstanceState.getInt("color")
        }

        txtSum.text = total.toString()
        txtSum.setTextColor(scoreColor)
        txtEle.text = getString(R.string.element_text, elementNum)

    }

    override fun onClick(v: View?) {

        //Routine completed pop up text
        fun routineCompleted() {
            Toast.makeText(this, getString(R.string.completed), Toast.LENGTH_LONG).show()
        }

        when (v?.id) {

            R.id.button_perform -> {
                if (!deducted && elementNum <= 10) {

                    when (elementNum) {
                        in 1..3 -> {
                            total += 1
                            scoreColor = Color.BLUE
                        }
                        in 4..7 -> {
                            total += 2
                            scoreColor = Color.GREEN
                        }
                        in 8..10 -> {
                            total += 3
                            scoreColor = Color.parseColor("#FFA500")
                        }
                    }

                    total = total.coerceAtMost(20)
                    elementNum++

                    Log.d("Checking", "Perform clicked")
                }
            }

            R.id.button_deduction -> {
                if (total in 1..<20) {
                    total -= 2
                    total = total.coerceAtLeast(0)
                    deducted = true
                    Log.d("Checking", "Deduction clicked")
                }
            }

            R.id.button_reset -> {
                total = 0
                elementNum = 1
                deducted = false
                scoreColor = Color.BLUE
                Log.d("Checking", "Reset clicked")
            }
        }

        if (elementNum > 10) {
            elementNum = 10
            routineCompleted()
        }

        //Displaying Score and Element
        txtEle.text = getString(R.string.element_text, elementNum)
        txtSum.text = total.toString()
        txtSum.setTextColor(scoreColor)

        Log.d("Checking", "Score updated: $total, Element: $elementNum")
    }

    //Saving Instances for rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("total", total)
        outState.putInt("element", elementNum)
        outState.putBoolean("deducted", deducted)
        outState.putInt("color", scoreColor)
        Log.d("Checking", "Saving Instances State")
    }

    //Change language function
    private fun changeLanguage(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        Log.d("Checking", "Language changed to $langCode")

        recreate()
    }
}