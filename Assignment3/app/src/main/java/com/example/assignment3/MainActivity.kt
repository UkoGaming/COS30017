package com.example.assignment3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.assignment3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var soundManager: SoundManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("Interaction", "MainActivity: Notification permission result: $isGranted")
        if (isGranted) {
            HabitNotificationHelper.scheduleDailyReminder(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        
        val primaryColor = prefs.getInt("primary_color", R.color.primary_blue)
        setThemeFromColor(primaryColor)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        soundManager = SoundManager(this)

        checkNotificationPermission()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            // Interaction: Tab Switching
            Log.d("Interaction", "MainActivity: Navigation tab selected: ${item.title}")
            soundManager.playClickSound()
            
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HabitListFragment())
                    true
                }
                R.id.navigation_report -> {
                    loadFragment(ReportFragment())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
                HabitNotificationHelper.scheduleDailyReminder(this)
            } else {
                Log.d("Interaction", "MainActivity: Requesting notification permission")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            HabitNotificationHelper.scheduleDailyReminder(this)
        }
    }

    private fun setThemeFromColor(colorRes: Int) {
        when (colorRes) {
            R.color.primary_blue -> setTheme(R.style.Theme_Assignment3_Blue)
            R.color.primary_green -> setTheme(R.style.Theme_Assignment3_Green)
            R.color.primary_purple -> setTheme(R.style.Theme_Assignment3_Purple)
            R.color.primary_orange -> setTheme(R.style.Theme_Assignment3_Orange)
            R.color.primary_pink -> setTheme(R.style.Theme_Assignment3_Pink)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}