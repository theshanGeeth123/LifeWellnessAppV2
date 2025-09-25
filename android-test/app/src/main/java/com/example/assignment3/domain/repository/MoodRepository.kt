package com.example.assignment3.domain.repository

import android.content.Context
import com.example.assignment3.data.prefs.MoodStore
import com.example.assignment3.domain.model.MoodEntry

class MoodRepository(ctx: Context) {
    private val store = MoodStore.get(ctx)

    fun list(): MutableList<MoodEntry> =
        store.getAll().sortedByDescending { it.timestampMillis }.toMutableList()

    fun add(emoji: String, note: String) {
        val all = store.getAll()
        all.add(
            MoodEntry(
                timestampMillis = System.currentTimeMillis(),
                emoji = emoji,
                note = note
            )
        )
        store.saveAll(all)
    }

    fun delete(id: String) {
        val updated = store.getAll().filterNot { it.id == id }
        store.saveAll(updated)
    }
}
