package com.example.assignment3.data.prefs

import android.content.Context

class HydrationPrefs(ctx: Context) {
    private val p = ctx.getSharedPreferences("hydration_prefs", Context.MODE_PRIVATE)

    fun isEnabled(): Boolean = p.getBoolean("enabled", false)
    fun setEnabled(v: Boolean) = p.edit().putBoolean("enabled", v).apply()

    // hours between reminders (min 1h for demo)
    fun intervalHours(): Int = p.getInt("interval_hours", 2)
    fun setIntervalHours(h: Int) = p.edit().putInt("interval_hours", h).apply()
}
