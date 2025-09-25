package com.example.assignment3.util

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.util.Calendar

object DatePickerHelper {
    fun show(
        ctx: Context,
        initial: LocalDate = DateProvider.today(),
        onPicked: (LocalDate) -> Unit
    ) {
        val cal = Calendar.getInstance().apply {
            set(initial.year, initial.monthValue - 1, initial.dayOfMonth)
        }

        val dialog = DatePickerDialog(
            ctx,
            { _, year, month, day ->
                val picked = LocalDate.of(year, month + 1, day)
                onPicked(picked)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // block future dates
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }
}
