package com.example.workshop2

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val viewModel: LightViewModel by viewModels()

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

        // Observe the light state from the ViewModel
        viewModel.lightState.observe(this) { _ ->
            updateUI(lightImageView)
        }

        lightImageView.setOnClickListener {
            viewModel.toggleLight()
        }

        lightImageView.setOnLongClickListener {
            viewModel.toggleLight()
            true
        }
    }

    private fun updateUI(imageView: ImageView) {
        val drawableId = viewModel.getDrawableId()
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, drawableId))
    }

}
