package com.example.assignment3.domain.repository

import android.content.Context
import com.example.assignment3.data.prefs.HabitStore
import com.example.assignment3.domain.model.Habit
import com.example.assignment3.domain.model.ProgressState
import com.example.assignment3.util.DateProvider
import java.time.LocalDate

class HabitRepository(ctx: Context) {
    private val store = HabitStore.get(ctx)

    // ---------- Catalog ----------
    fun loadHabits(): MutableList<Habit> = store.getHabits()

    fun addHabit(title: String) {
        val list = store.getHabits()
        list.add(Habit(title = title))
        store.saveHabits(list)
    }

    fun updateHabitTitle(id: String, newTitle: String) {   // <--- REQUIRED
        val list = store.getHabits()
        val idx = list.indexOfFirst { it.id == id }
        if (idx >= 0) {
            list[idx] = list[idx].copy(title = newTitle)
            store.saveHabits(list)
        }
    }

    fun deleteHabit(id: String) {
        val updated = store.getHabits().filterNot { it.id == id }
        store.saveHabits(updated)

        // also clean from today's done set
        val today = DateProvider.today()
        val done = store.getCompletedIds(today)
        if (done.remove(id)) {
            store.setCompletedIds(today, done)
        }
    }

    // ---------- Per-date completion ----------
    fun isDone(date: LocalDate, id: String): Boolean {
        val done = store.getCompletedIds(date)
        return done.contains(id)
    }

    fun toggleDone(date: LocalDate, id: String) {
        val done = store.getCompletedIds(date)
        if (done.contains(id)) done.remove(id) else done.add(id)
        store.setCompletedIds(date, done)
    }

    fun progress(date: LocalDate): ProgressState {
        val total = store.getHabits().size
        val done = store.getCompletedIds(date).size
        return ProgressState(done, total)
    }

    // ---------- Convenience for "today" ----------
    fun isDoneToday(id: String) = isDone(DateProvider.today(), id)
    fun toggleDoneToday(id: String) = toggleDone(DateProvider.today(), id)
    fun progressToday() = progress(DateProvider.today())
}
