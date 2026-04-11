package com.example.assignment3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText

class AddEditHabitFragment : Fragment() {

    private lateinit var edtHabitName: TextInputEditText
    private lateinit var edtHabitDescription: TextInputEditText
    private lateinit var checkIsProgress: MaterialSwitch
    private lateinit var edtTargetValue: TextInputEditText
    private lateinit var layoutTarget: LinearLayout
    private lateinit var spinnerUnit: Spinner
    private lateinit var checkIsManual: MaterialSwitch
    
    private var habit: Habit? = null
    private val viewModel: HabitViewModel by activityViewModels()
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = it.getSerializable("habit") as? Habit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_habit, container, false)
        soundManager = SoundManager(requireContext())

        edtHabitName = view.findViewById(R.id.edtHabitName)
        edtHabitDescription = view.findViewById(R.id.edtHabitDescription)
        checkIsProgress = view.findViewById(R.id.checkIsProgress)
        edtTargetValue = view.findViewById(R.id.edtTargetValue)
        layoutTarget = view.findViewById(R.id.layoutTarget)
        spinnerUnit = view.findViewById(R.id.spinnerUnit)
        checkIsManual = view.findViewById(R.id.checkIsManual)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)

        // Removed "workout" from the units list
        val units = arrayOf("steps", "hours", "ml", "kcal")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnit.adapter = adapter

        checkIsProgress.setOnCheckedChangeListener { _, isChecked ->
            // Interaction: Toggle Progression
            Log.d("Interaction", "AddEditHabitFragment: Progression toggled to $isChecked")
            soundManager.playClickSound()
            layoutTarget.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        habit?.let {
            edtHabitName.setText(it.name)
            edtHabitDescription.setText(it.description)
            checkIsProgress.isChecked = it.isProgressHabit
            if (it.isProgressHabit) {
                edtTargetValue.setText(it.targetValue.toString())
                layoutTarget.visibility = View.VISIBLE
                checkIsManual.isChecked = it.isManualProgress
                val unitIndex = units.indexOf(it.unit)
                if (unitIndex >= 0) spinnerUnit.setSelection(unitIndex)
            }
        }

        btnSave.setOnClickListener {
            // Interaction: Save Habit
            Log.d("Interaction", "AddEditHabitFragment: Save Habit button clicked")
            soundManager.playClickSound()
            val name = edtHabitName.text.toString()
            val description = edtHabitDescription.text.toString()
            val isProgress = checkIsProgress.isChecked
            val targetStr = edtTargetValue.text.toString()
            val target = if (targetStr.isNotEmpty()) targetStr.toInt() else 5000
            val unit = spinnerUnit.selectedItem.toString()
            val isManual = checkIsManual.isChecked
            
            if (name.isNotEmpty()) {
                val currentHabits = viewModel.habits.value ?: emptyList()
                val existingHabit = currentHabits.find { it.id == habit?.id }

                if (existingHabit == null) {
                    val icon = habit?.icon ?: "⭐"
                    viewModel.addHabit(Habit(
                        name = name, 
                        description = description, 
                        icon = icon,
                        isProgressHabit = isProgress,
                        isManualProgress = isManual,
                        targetValue = target,
                        unit = unit
                    ))
                } else {
                    val updatedHabit = existingHabit.copy(
                        name = name, 
                        description = description,
                        isProgressHabit = isProgress,
                        isManualProgress = isManual,
                        targetValue = target,
                        unit = unit
                    )
                    viewModel.updateHabit(updatedHabit)
                }
                
                val hasHabitList = parentFragmentManager.backStackEntryCount > 0 && 
                                   (0 until parentFragmentManager.backStackEntryCount).any { 
                                       parentFragmentManager.getBackStackEntryAt(it).name == "habit_list" 
                                   }

                if (hasHabitList) {
                    parentFragmentManager.popBackStack("habit_list", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    parentFragmentManager.popBackStack()
                }
            }
        }

        return view
    }

    companion object {
        fun newInstance(habit: Habit? = null) = AddEditHabitFragment().apply {
            arguments = Bundle().apply {
                putSerializable("habit", habit)
            }
        }
    }
}