package com.example.assignment3.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.assignment3.R
import com.example.assignment3.data.prefs.HydrationPrefs
import com.example.assignment3.notifications.NotificationHelper
import com.example.assignment3.util.ReminderScheduler
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment() {

    private lateinit var prefs: HydrationPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = HydrationPrefs(requireContext())
        NotificationHelper.ensureChannel(requireContext())

        val sw = view.findViewById<Switch>(R.id.switchEnable)
        val npHours = view.findViewById<NumberPicker>(R.id.npHours)
        val btnTestNow = view.findViewById<MaterialButton>(R.id.btnTestNow)

        val npTestMin = view.findViewById<NumberPicker>(R.id.npTestMinutes)
        val btnTestAfterMin = view.findViewById<MaterialButton>(R.id.btnTestAfterMinutes)

        // Hours picker (real schedule)
        npHours.minValue = 1
        npHours.maxValue = 24
        npHours.value = prefs.intervalHours()

        // Initial state
        sw.isChecked = prefs.isEnabled()

        sw.setOnCheckedChangeListener { _, checked ->
            prefs.setEnabled(checked)
            ReminderScheduler.ensureScheduled(requireContext())
        }

        npHours.setOnValueChangedListener { _, _, newVal ->
            prefs.setIntervalHours(newVal)
            ReminderScheduler.ensureScheduled(requireContext())
        }

        // Immediate “test now” notification
        btnTestNow.setOnClickListener {
            NotificationHelper.showHydration(requireContext())
            Toast.makeText(requireContext(), "Test notification sent", Toast.LENGTH_SHORT).show()
        }

        // Minutes picker for one-time test
        npTestMin.minValue = 1
        npTestMin.maxValue = 10
        npTestMin.value = 1

        btnTestAfterMin.setOnClickListener {
            val mins = npTestMin.value
            ReminderScheduler.scheduleTestInMinutes(requireContext(), mins)
            Toast.makeText(requireContext(), "One-time test scheduled in $mins minute(s)", Toast.LENGTH_SHORT).show()
        }
    }
}
