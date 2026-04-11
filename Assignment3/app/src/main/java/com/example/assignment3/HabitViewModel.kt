package com.example.assignment3

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val healthConnectManager = HealthConnectManager(application)

    private val _habits = MutableLiveData<List<Habit>>(loadHabits())
    val habits: LiveData<List<Habit>> = _habits

    init {
        refreshHabits()
    }

    private fun loadHabits(): List<Habit> {
        val json = sharedPreferences.getString("habits_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Habit>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveHabits() {
        val json = gson.toJson(_habits.value)
        sharedPreferences.edit().putString("habits_list", json).apply()
    }

    fun addHabit(habit: Habit) {
        val currentList = _habits.value?.toMutableList() ?: mutableListOf()
        currentList.add(habit)
        _habits.value = currentList
        saveHabits()
    }

    fun updateHabit(updatedHabit: Habit) {
        val currentList = _habits.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            currentList[index] = updatedHabit
            _habits.value = currentList
            saveHabits()
        }
    }

    fun deleteHabit(habitId: String) {
        val currentList = _habits.value?.toMutableList() ?: return
        currentList.removeAll { it.id == habitId }
        _habits.value = currentList
        saveHabits()
    }

    fun updateProgress(habitId: String, progress: Int) {
        val currentList = _habits.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == habitId }
        if (index != -1) {
            val h = currentList[index]
            val today = getStartOfDay()
            
            var newHabit = h.copy(currentProgress = progress)
            if (progress >= h.targetValue) {
                newHabit = markAsDoneToday(newHabit, today)
            } else {
                newHabit = unmarkAsDoneToday(newHabit, today)
            }
            
            newHabit.streak = calculateStreak(newHabit.completedDates)
            currentList[index] = newHabit
            _habits.value = currentList
            saveHabits()
        }
    }

    fun toggleHabitDone(habit: Habit, isDone: Boolean) {
        val currentList = _habits.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            val h = currentList[index]
            val today = getStartOfDay()
            val newCompletedDates = h.completedDates.toMutableSet()

            if (isDone) {
                newCompletedDates.add(today)
            } else {
                newCompletedDates.remove(today)
            }

            val newStreak = calculateStreak(newCompletedDates)
            currentList[index] = h.copy(
                isDone = isDone,
                streak = newStreak,
                completedDates = newCompletedDates
            )
            _habits.value = currentList
            saveHabits()
        }
    }

    private fun calculateStreak(dates: Set<Long>): Int {
        if (dates.isEmpty()) return 0
        val sortedDates = dates.sortedDescending()
        val today = getStartOfDay()
        val yesterday = getYesterdayStart()
        
        var streak = 0
        var expectedDate = if (dates.contains(today)) today else yesterday
        
        for (date in sortedDates) {
            if (date == expectedDate) {
                streak++
                expectedDate = getPreviousDayStart(expectedDate)
            } else if (date < expectedDate) {
                break
            }
        }
        return streak
    }

    fun refreshHabits() {
        viewModelScope.launch {
            val currentList = _habits.value?.toMutableList() ?: return@launch
            val today = getStartOfDay()
            var changed = false

            val hasHealthPermissions = healthConnectManager.hasAllPermissions()
            val startTime = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).toInstant()
            val endTime = Instant.now()

            val steps = if (hasHealthPermissions) healthConnectManager.readSteps(startTime, endTime) else 0
            val workouts = if (hasHealthPermissions) healthConnectManager.readWorkouts(startTime, endTime) else 0

            currentList.forEachIndexed { index, habit ->
                var h = habit
                
                // New day logic: reset manual progress
                if (h.lastCompletedDate != null && h.lastCompletedDate!! < today) {
                    if (h.isManualProgress) {
                        h.currentProgress = 0
                        changed = true
                    }
                }

                if (h.isProgressHabit && !h.isManualProgress && hasHealthPermissions) {
                    val progress = if (h.name.contains("Walk", true) || h.name.contains("Step", true)) {
                        steps.toInt()
                    } else if (h.name.contains("Calorie", true)) {
                        // Assuming health connect gives kcal, mapping for demo
                        steps.toInt() / 20 
                    } else {
                        workouts * (h.targetValue / 1)
                    }
                    
                    h.currentProgress = progress
                    if (progress >= h.targetValue) {
                        h = markAsDoneToday(h, today)
                    } else {
                        h = unmarkAsDoneToday(h, today)
                    }
                }

                val isDoneToday = h.completedDates.contains(today)
                if (h.isDone != isDoneToday || h.currentProgress != habit.currentProgress || h != habit) {
                    currentList[index] = h.copy(isDone = isDoneToday, streak = calculateStreak(h.completedDates))
                    changed = true
                }
            }

            if (changed) {
                _habits.value = currentList
                saveHabits()
            }
        }
    }

    private fun markAsDoneToday(habit: Habit, today: Long): Habit {
        if (habit.completedDates.contains(today)) return habit
        val newDates = habit.completedDates.toMutableSet()
        newDates.add(today)
        return habit.copy(completedDates = newDates, isDone = true)
    }

    private fun unmarkAsDoneToday(habit: Habit, today: Long): Habit {
        if (!habit.completedDates.contains(today)) return habit
        val newDates = habit.completedDates.toMutableSet()
        newDates.remove(today)
        return habit.copy(completedDates = newDates, isDone = false)
    }

    fun getDaysWithAllHabitsDone(): Set<Long> {
        val habits = _habits.value ?: return emptySet()
        if (habits.isEmpty()) return emptySet()

        val allDates = habits.flatMap { it.completedDates }.toSet()
        return allDates.filter { date ->
            // Filter habits that existed on this date
            val habitsOnDate = habits.filter { habit ->
                val habitStartCal = Calendar.getInstance()
                habitStartCal.time = habit.startDate
                habitStartCal.set(Calendar.HOUR_OF_DAY, 0)
                habitStartCal.set(Calendar.MINUTE, 0)
                habitStartCal.set(Calendar.SECOND, 0)
                habitStartCal.set(Calendar.MILLISECOND, 0)
                habitStartCal.timeInMillis <= date
            }
            habitsOnDate.isNotEmpty() && habitsOnDate.all { it.completedDates.contains(date) }
        }.toSet()
    }

    private fun getStartOfDay(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getYesterdayStart(): Long = getPreviousDayStart(getStartOfDay())

    private fun getPreviousDayStart(millis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return cal.timeInMillis
    }
}