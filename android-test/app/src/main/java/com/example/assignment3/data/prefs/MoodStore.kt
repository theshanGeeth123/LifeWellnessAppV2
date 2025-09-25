package com.example.assignment3.data.prefs

import android.content.Context
import com.example.assignment3.domain.model.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoodStore private constructor(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("mood_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        @Volatile private var INSTANCE: MoodStore? = null
        fun get(ctx: Context): MoodStore =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MoodStore(ctx.applicationContext).also { INSTANCE = it }
            }
    }

    fun getAll(): MutableList<MoodEntry> {
        val json = prefs.getString("mood_entries_json", "[]")
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveAll(list: List<MoodEntry>) {
        prefs.edit().putString("mood_entries_json", gson.toJson(list)).apply()
    }
}
