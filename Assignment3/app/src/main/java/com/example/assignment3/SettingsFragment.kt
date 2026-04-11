package com.example.assignment3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class SettingsFragment : Fragment() {

    private lateinit var imgAvatar: ImageView
    private lateinit var edtUserName: TextInputEditText
    private var selectedImageUri: Uri? = null
    private lateinit var soundManager: SoundManager

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val internalUri = saveImageToInternalStorage(it)
            if (internalUri != null) {
                selectedImageUri = internalUri
                imgAvatar.setImageURI(internalUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        soundManager = SoundManager(requireContext())

        imgAvatar = view.findViewById(R.id.imgAvatar)
        edtUserName = view.findViewById(R.id.edtUserName)
        val btnChangePhoto = view.findViewById<MaterialButton>(R.id.btnChangePhoto)
        val btnSaveSettings = view.findViewById<MaterialButton>(R.id.btnSaveSettings)
        val btnManageHabits = view.findViewById<LinearLayout>(R.id.btnManageHabits)
        val switchDarkMode = view.findViewById<MaterialSwitch>(R.id.switchDarkMode)
        val switchSound = view.findViewById<MaterialSwitch>(R.id.switchSound)
        val layoutColorPicker = view.findViewById<LinearLayout>(R.id.layoutColorPicker)

        val prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        
        edtUserName.setText(prefs.getString("user_name", "Guest"))
        val savedImageUriString = prefs.getString("user_avatar", null)
        if (savedImageUriString != null) {
            val uri = Uri.parse(savedImageUriString)
            if (File(uri.path!!).exists()) {
                imgAvatar.setImageURI(uri)
            } else {
                imgAvatar.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        // Theme
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        switchDarkMode.isChecked = isDarkMode
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            soundManager.playClickSound()
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Sounds
        val isSoundEnabled = prefs.getBoolean("app_sounds", true)
        switchSound.isChecked = isSoundEnabled
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("app_sounds", isChecked).apply()
            // Play a test sound if turning ON
            if (isChecked) soundManager.playClickSound()
        }

        val colors = listOf(
            R.color.primary_blue,
            R.color.primary_green,
            R.color.primary_purple,
            R.color.primary_orange,
            R.color.primary_pink
        )

        colors.forEach { colorRes ->
            val colorView = View(requireContext())
            val size = (40 * resources.displayMetrics.density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.setMargins(8, 0, 8, 0)
            colorView.layoutParams = params
            colorView.setBackgroundResource(R.drawable.circle_color)
            colorView.backgroundTintList = resources.getColorStateList(colorRes, null)
            
            colorView.setOnClickListener {
                soundManager.playClickSound()
                prefs.edit().putInt("primary_color", colorRes).apply()
                requireActivity().recreate()
            }
            layoutColorPicker.addView(colorView)
        }

        btnChangePhoto.setOnClickListener { 
            soundManager.playClickSound()
            pickImage.launch("image/*") 
        }

        btnSaveSettings.setOnClickListener {
            soundManager.playClickSound()
            val newName = edtUserName.text.toString()
            if (newName.isNotEmpty()) {
                val editor = prefs.edit()
                editor.putString("user_name", newName)
                selectedImageUri?.let { editor.putString("user_avatar", it.toString()) }
                editor.apply()
                Toast.makeText(context, "Profile Saved!", Toast.LENGTH_SHORT).show()
            }
        }

        btnManageHabits.setOnClickListener {
            soundManager.playClickSound()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ManageHabitsFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun saveImageToInternalStorage(uri: Uri): Uri? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "profile_avatar.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}