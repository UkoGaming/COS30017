package com.example.assignment3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3.databinding.FragmentHabitListBinding

class HabitListFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels()
    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!
    private var habitAdapter: HabitAdapter? = null
    private lateinit var soundManager: SoundManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        soundManager = SoundManager(requireContext())

        binding.recyclerHabits.layoutManager = LinearLayoutManager(requireContext())
        
        habitAdapter = HabitAdapter(
            onItemClick = { habit ->
                Log.d("Interaction", "HabitListFragment: Habit clicked: ${habit.name}")
                soundManager.playClickSound()
                val fragment = HabitDetailFragment.newInstance(habit)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("habit_detail")
                    .commit()
            },
            onDoneChanged = { habit, isDone ->
                Log.d("Interaction", "HabitListFragment: Habit done toggled: ${habit.name} to $isDone")
                soundManager.playClickSound()
                viewModel.toggleHabitDone(habit, isDone)
            },
            onProgressChanged = { habit, progress ->
                Log.d("Interaction", "HabitListFragment: Habit progress changed: ${habit.name} to $progress")
                viewModel.updateProgress(habit.id, progress)
            }
        )
        binding.recyclerHabits.adapter = habitAdapter

        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            habitAdapter?.submitList(habits.toList())
        }

        binding.btnAddHabit.setOnClickListener {
            Log.d("Interaction", "HabitListFragment: Add Habit FAB clicked")
            soundManager.playClickSound()
            val fragment = HabitSuggestionsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("habit_list")
                .commit()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshHabits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        habitAdapter = null
    }
}