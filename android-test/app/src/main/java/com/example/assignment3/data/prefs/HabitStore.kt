package com.example.assignment3.data.prefs

import android.content.Context
import com.example.assignment3.domain.model.Habit
import com.example.assignment3.util.DateProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class HabitStore private constructor(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        @Volatile private var INSTANCE: HabitStore? = null

        fun get(ctx: Context): HabitStore =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HabitStore(ctx.applicationContext).also { INSTANCE = it }
            }
    }

    // ---- Habits catalog ----
    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString("habits_json", "[]")
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveHabits(list: List<Habit>) {
        prefs.edit().putString("habits_json", gson.toJson(list)).apply()
    }

    // ---- Today's completion IDs ----
    private fun keyFor(date: LocalDate) = "done_${DateProvider.keyFor(date)}"

    fun getCompletedIds(date: LocalDate): MutableSet<String> {
        return prefs.getStringSet(keyFor(date), emptySet())!!.toMutableSet()
    }

    fun setCompletedIds(date: LocalDate, ids: Set<String>) {
        prefs.edit().putStringSet(keyFor(date), ids).apply()
    }
}
