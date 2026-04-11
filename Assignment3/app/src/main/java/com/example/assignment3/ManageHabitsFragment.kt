package com.example.assignment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManageHabitsFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_habits, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerManageHabits)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            recyclerView.adapter = object : RecyclerView.Adapter<ManageHabitViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageHabitViewHolder {
                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_manage_habit, parent, false)
                    return ManageHabitViewHolder(itemView)
                }

                override fun onBindViewHolder(holder: ManageHabitViewHolder, position: Int) {
                    val habit = habits[position]
                    holder.txtName.text = habit.name
                    holder.btnDelete.setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Delete Habit")
                            .setMessage("Are you sure you want to delete '${habit.name}'?")
                            .setPositiveButton("Delete") { _, _ ->
                                viewModel.deleteHabit(habit.id)
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }

                override fun getItemCount() = habits.size
            }
        }

        return view
    }

    class ManageHabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtManageHabitName)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteHabit)
    }
}