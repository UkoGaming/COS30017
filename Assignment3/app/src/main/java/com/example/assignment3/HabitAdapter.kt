package com.example.assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(
    private val onItemClick: (Habit) -> Unit,
    private val onDoneChanged: (Habit, Boolean) -> Unit,
    private val onProgressChanged: (Habit, Int) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.bind(habit, onItemClick, onDoneChanged, onProgressChanged)
    }

    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtIcon: TextView = view.findViewById(R.id.txtHabitIcon)
        private val txtName: TextView = view.findViewById(R.id.txtName)
        private val txtStreak: TextView = view.findViewById(R.id.txtStreak)
        private val checkDone: CheckBox = view.findViewById(R.id.checkDone)
        private val layoutProgress: LinearLayout = view.findViewById(R.id.layoutProgress)
        private val progressBar: ProgressBar = view.findViewById(R.id.progressHabit)
        private val seekProgress: SeekBar = view.findViewById(R.id.seekManualProgress)
        private val txtProgress: TextView = view.findViewById(R.id.txtProgressValue)

        fun bind(
            habit: Habit,
            onItemClick: (Habit) -> Unit,
            onDoneChanged: (Habit, Boolean) -> Unit,
            onProgressChanged: (Habit, Int) -> Unit
        ) {
            txtIcon.text = habit.icon
            txtName.text = habit.name
            txtStreak.text = "🔥 Streak: ${habit.streak} days"

            if (habit.isProgressHabit) {
                checkDone.visibility = View.GONE
                layoutProgress.visibility = View.VISIBLE
                progressBar.max = habit.targetValue
                progressBar.progress = habit.currentProgress
                txtProgress.text = "${habit.currentProgress} / ${habit.targetValue} ${habit.unit}"

                if (habit.isManualProgress) {
                    progressBar.visibility = View.GONE
                    seekProgress.visibility = View.VISIBLE
                    seekProgress.max = habit.targetValue
                    seekProgress.progress = habit.currentProgress
                    
                    seekProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            if (fromUser) {
                                txtProgress.text = "$progress / ${habit.targetValue} ${habit.unit}"
                            }
                        }
                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            onProgressChanged(habit, seekProgress.progress)
                        }
                    })
                } else {
                    progressBar.visibility = View.VISIBLE
                    seekProgress.visibility = View.GONE
                }
            } else {
                checkDone.visibility = View.VISIBLE
                layoutProgress.visibility = View.GONE
                checkDone.setOnCheckedChangeListener(null)
                checkDone.isChecked = habit.isDone
                checkDone.setOnCheckedChangeListener { _, isChecked ->
                    onDoneChanged(habit, isChecked)
                }
            }

            itemView.setOnClickListener { onItemClick(habit) }
        }
    }

    private class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean =
            oldItem == newItem
    }
}