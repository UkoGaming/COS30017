package com.example.assignment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels()
    private lateinit var txtMonthYear: TextView
    private lateinit var recyclerCalendar: RecyclerView
    private lateinit var txtDayStatus: TextView
    private lateinit var txtReportSummary: TextView
    
    private var currentCalendar = Calendar.getInstance()
    private val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        txtMonthYear = view.findViewById(R.id.txtMonthYear)
        recyclerCalendar = view.findViewById(R.id.recyclerCalendar)
        txtDayStatus = view.findViewById(R.id.txtDayStatus)
        txtReportSummary = view.findViewById(R.id.txtReportSummary)
        val btnPrev = view.findViewById<MaterialButton>(R.id.btnPrevMonth)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNextMonth)

        recyclerCalendar.layoutManager = GridLayoutManager(requireContext(), 7)

        viewModel.habits.observe(viewLifecycleOwner) {
            val perfectDays = viewModel.getDaysWithAllHabitsDone()
            txtReportSummary.text = "Perfect Days: ${perfectDays.size}"
            updateCalendar()
        }

        btnPrev.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        btnNext.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        updateCalendar()

        return view
    }

    private fun updateCalendar() {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        txtMonthYear.text = sdf.format(currentCalendar.time)

        val daysInMonth = mutableListOf<Long?>()
        val calendar = currentCalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 0 until firstDayOfWeek) {
            daysInMonth.add(null)
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            daysInMonth.add(calendar.timeInMillis)
        }

        recyclerCalendar.adapter = CalendarAdapter(daysInMonth, viewModel.getDaysWithAllHabitsDone()) { date ->
            showDayDetailsDialog(date)
        }
    }

    private fun showDayDetailsDialog(date: Long) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_day_details, null)
        val txtDialogDate = dialogView.findViewById<TextView>(R.id.txtDialogDate)
        val recyclerDayHabits = dialogView.findViewById<RecyclerView>(R.id.recyclerDayHabits)
        val btnClose = dialogView.findViewById<MaterialButton>(R.id.btnCloseDialog)

        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        txtDialogDate.text = sdf.format(date)

        val habits = viewModel.habits.value ?: emptyList()
        
        // Filter habits: only show habits that existed on or before the selected date
        val filteredHabits = habits.filter { habit ->
            val habitStartCal = Calendar.getInstance()
            habitStartCal.time = habit.startDate
            habitStartCal.set(Calendar.HOUR_OF_DAY, 0)
            habitStartCal.set(Calendar.MINUTE, 0)
            habitStartCal.set(Calendar.SECOND, 0)
            habitStartCal.set(Calendar.MILLISECOND, 0)
            habitStartCal.timeInMillis <= date
        }

        recyclerDayHabits.layoutManager = LinearLayoutManager(requireContext())
        recyclerDayHabits.adapter = DayHabitStatusAdapter(filteredHabits, date)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private inner class DayHabitStatusAdapter(private val habits: List<Habit>, private val date: Long) :
        RecyclerView.Adapter<DayHabitStatusViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHabitStatusViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day_habit_status, parent, false)
            return DayHabitStatusViewHolder(view)
        }

        override fun onBindViewHolder(holder: DayHabitStatusViewHolder, position: Int) {
            val habit = habits[position]
            holder.txtIcon.text = habit.icon
            holder.txtName.text = habit.name
            
            val isDone = habit.completedDates.contains(date)
            if (isDone) {
                holder.imgStatus.setImageResource(R.drawable.circle_color)
                holder.imgStatus.imageTintList = resources.getColorStateList(android.R.color.holo_green_dark, null)
            } else {
                holder.imgStatus.setImageResource(R.drawable.ic_minus)
                holder.imgStatus.imageTintList = resources.getColorStateList(android.R.color.darker_gray, null)
            }
        }

        override fun getItemCount() = habits.size
    }

    private class DayHabitStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtIcon: TextView = view.findViewById(R.id.txtHabitIcon)
        val txtName: TextView = view.findViewById(R.id.txtHabitName)
        val imgStatus: ImageView = view.findViewById(R.id.imgStatusIcon)
    }

    private inner class CalendarAdapter(
        private val days: List<Long?>,
        private val perfectDays: Set<Long>,
        private val onDayClick: (Long) -> Unit
    ) : RecyclerView.Adapter<CalendarViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
            return CalendarViewHolder(view)
        }

        override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
            val date = days[position]
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.timeInMillis = date
                holder.txtDay.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                
                when {
                    perfectDays.contains(date) -> {
                        holder.cardDay.setCardBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                        holder.txtDay.setTextColor(resources.getColor(android.R.color.white, null))
                        holder.cardDay.strokeWidth = 0
                    }
                    date == today -> {
                        holder.cardDay.setCardBackgroundColor(resources.getColor(android.R.color.transparent, null))
                        holder.txtDay.setTextColor(resources.getColor(R.color.primary_blue, null))
                        holder.cardDay.strokeWidth = 2
                        holder.cardDay.setStrokeColor(resources.getColorStateList(R.color.primary_blue, null))
                    }
                    else -> {
                        holder.cardDay.setCardBackgroundColor(resources.getColor(android.R.color.transparent, null))
                        holder.txtDay.setTextColor(resources.getColor(android.R.color.tab_indicator_text, null))
                        holder.cardDay.strokeWidth = 0
                    }
                }

                holder.itemView.setOnClickListener { onDayClick(date) }
            } else {
                holder.txtDay.text = ""
                holder.cardDay.setCardBackgroundColor(resources.getColor(android.R.color.transparent, null))
                holder.cardDay.strokeWidth = 0
            }
        }

        override fun getItemCount() = days.size
    }

    private class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDay: TextView = view.findViewById(R.id.txtDayNumber)
        val cardDay: MaterialCardView = view.findViewById(R.id.cardDay)
    }
}