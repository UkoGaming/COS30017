package com.example.workshop2

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var isLightOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lightImageView = findViewById<ImageView>(R.id.lightImageView)

        lightImageView.setOnClickListener {
            toggleLight(lightImageView)
        }

        lightImageView.setOnLongClickListener {
            toggleLight(lightImageView)
            true
        }
    }

    private fun toggleLight(imageView: ImageView) {
        isLightOn = !isLightOn
        val drawableId = if (isLightOn) R.drawable.ic_light_on else R.drawable.ic_light_off
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, drawableId))
    }
}
