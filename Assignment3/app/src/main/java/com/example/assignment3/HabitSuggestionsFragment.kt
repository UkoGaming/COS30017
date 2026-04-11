package com.example.assignment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3.databinding.FragmentHabitSuggestionsBinding
import com.google.android.material.button.MaterialButton

class HabitSuggestionsFragment : Fragment() {

    private var _binding: FragmentHabitSuggestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var soundManager: SoundManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitSuggestionsBinding.inflate(inflater, container, false)
        soundManager = SoundManager(requireContext())

        val suggestions = listOf(
            HabitSuggestion("Walk", "🚶", isProgress = true, isManual = false, target = 5000, unit = "steps"),
            HabitSuggestion("Sleep", "🛌", isProgress = true, isManual = true, target = 8, unit = "hours"),
            HabitSuggestion("Drink water", "💧", isProgress = true, isManual = true, target = 2000, unit = "ml"),
            HabitSuggestion("Run", "🏃", isProgress = true, isManual = false, target = 1, unit = "workout"),
            HabitSuggestion("Workout", "💪", isProgress = true, isManual = false, target = 1, unit = "workout"),
            HabitSuggestion("Active Calorie", "🔥", isProgress = true, isManual = false, target = 500, unit = "kcal"),
            HabitSuggestion("Burn Calorie", "🔥", isProgress = true, isManual = false, target = 2000, unit = "kcal")
        )

        binding.recyclerSuggestions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSuggestions.adapter = SuggestionAdapter(suggestions) { suggestion ->
            soundManager.playClickSound()
            val habit = Habit(
                name = suggestion.name,
                icon = suggestion.emoji,
                isProgressHabit = suggestion.isProgress,
                isManualProgress = suggestion.isManual,
                targetValue = suggestion.target,
                unit = suggestion.unit
            )
            val fragment = AddEditHabitFragment.newInstance(habit)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.btnCustomHabit.setOnClickListener {
            soundManager.playClickSound()
            val fragment = AddEditHabitFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}