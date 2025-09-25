package com.example.assignment3.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.assignment3.data.prefs.HydrationPrefs
import com.example.assignment3.workers.HydrationWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    private const val UNIQUE_PERIODIC = "hydration_periodic"
    private const val UNIQUE_TEST_ONETIME = "hydration_test_once"

    /** Real schedule (hours) — used in production/demo. */
    fun ensureScheduled(ctx: Context) {
        val prefs = HydrationPrefs(ctx)
        val wm = WorkManager.getInstance(ctx)

        if (!prefs.isEnabled()) {
            wm.cancelUniqueWork(UNIQUE_PERIODIC)
            return
        }

        val hours = prefs.intervalHours().coerceAtLeast(1)
        // WorkManager requires min 15 min for periodic requests.
        val req = PeriodicWorkRequestBuilder<HydrationWorker>(
            hours.toLong(), TimeUnit.HOURS
        ).build()

        wm.enqueueUniquePeriodicWork(
            UNIQUE_PERIODIC,
            ExistingPeriodicWorkPolicy.UPDATE,
            req
        )
    }

    /** Cancel periodic schedule. */
    fun cancel(ctx: Context) {
        WorkManager.getInstance(ctx).cancelUniqueWork(UNIQUE_PERIODIC)
    }

    /** Testing helper: run once after [minutes] delay (1–10 recommended). */
    fun scheduleTestInMinutes(ctx: Context, minutes: Int) {
        val m = minutes.coerceAtLeast(1)
        val req = OneTimeWorkRequestBuilder<HydrationWorker>()
            .setInitialDelay(m.toLong(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(ctx).enqueueUniqueWork(
            UNIQUE_TEST_ONETIME,
            ExistingWorkPolicy.REPLACE,
            req
        )
    }
}
