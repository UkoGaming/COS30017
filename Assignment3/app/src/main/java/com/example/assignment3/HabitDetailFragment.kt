package com.example.assignment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.text.SimpleDateFormat
import java.util.Locale

class HabitDetailFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_habit_detail, container, false)
        soundManager = SoundManager(requireContext())

        val txtName = view.findViewById<TextView>(R.id.txtDetailName)
        val txtDescription = view.findViewById<TextView>(R.id.txtDetailDescription)
        val txtStreak = view.findViewById<TextView>(R.id.txtDetailStreak)
        val txtStartDate = view.findViewById<TextView>(R.id.txtStartDate)
        val btnMarkDone = view.findViewById<Button>(R.id.btnMarkDone)
        val btnEditHabit = view.findViewById<Button>(R.id.btnEditHabit)

        val layoutProgress = view.findViewById<LinearLayout>(R.id.layoutProgressSection)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressDetail)
        val txtProgressValue = view.findViewById<TextView>(R.id.txtProgressValue)
        val txtProgressPercentage = view.findViewById<TextView>(R.id.txtProgressPercentage)

        habit?.let { h ->
            txtName.text = h.name
            txtDescription.text = if (h.description.isEmpty()) "No description provided." else h.description
            txtStreak.text = h.streak.toString()
            
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            txtStartDate.text = sdf.format(h.startDate)

            if (h.isProgressHabit) {
                btnMarkDone.visibility = View.GONE
                layoutProgress.visibility = View.VISIBLE
                progressBar.max = h.targetValue
                progressBar.progress = h.currentProgress
                txtProgressValue.text = "${h.currentProgress} / ${h.targetValue}"
                val percent = if (h.targetValue > 0) (h.currentProgress * 100) / h.targetValue else 0
                txtProgressPercentage.text = "$percent%"
            } else {
                btnMarkDone.visibility = View.VISIBLE
                layoutProgress.visibility = View.GONE
                btnMarkDone.isEnabled = !h.isDone
                btnMarkDone.text = if (h.isDone) "Completed Today" else "Mark as Done"
            }
        }

        btnMarkDone.setOnClickListener {
            soundManager.playClickSound()
            habit?.let { h ->
                viewModel.toggleHabitDone(h, true)
                txtStreak.text = (h.streak + 1).toString()
                btnMarkDone.isEnabled = false
                btnMarkDone.text = "Completed Today"
            }
        }

        btnEditHabit.setOnClickListener {
            soundManager.playClickSound()
            habit?.let { h ->
                val fragment = AddEditHabitFragment.newInstance(h)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    companion object {
        fun newInstance(habit: Habit) = HabitDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("habit", habit)
            }
        }
    }
}