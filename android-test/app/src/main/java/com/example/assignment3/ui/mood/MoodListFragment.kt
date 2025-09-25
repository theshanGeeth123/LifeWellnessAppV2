package com.example.assignment3.ui.mood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.domain.repository.MoodRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MoodListFragment : Fragment() {

    private lateinit var repo: MoodRepository
    private lateinit var adapter: MoodAdapter

    private lateinit var rvMood: RecyclerView
    private lateinit var fabAddMood: FloatingActionButton
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout WITHOUT viewBinding
        return inflater.inflate(R.layout.fragment_mood_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)

            // find views (ids must exist in fragment_mood_list.xml)
            rvMood = view.findViewById(R.id.rvMood)
            fabAddMood = view.findViewById(R.id.fabAddMood)
            tvEmpty = view.findViewById(R.id.tvEmpty)

            repo = MoodRepository(requireContext())

            adapter = MoodAdapter(
                items = repo.list(),
                onDelete = { entry ->
                    repo.delete(entry.id)
                    refreshList()
                }
            )

            rvMood.layoutManager = LinearLayoutManager(requireContext())
            rvMood.adapter = adapter

            fabAddMood.setOnClickListener { showAddDialog() }

            refreshList()
        } catch (e: Exception) {
            Log.e("MoodListFragment", "onViewCreated crash", e)
            Toast.makeText(requireContext(), "Mood screen error: ${e.message}", Toast.LENGTH_LONG).show()
            throw e
        }
    }

    private fun refreshList() {
        val data = repo.list()
        adapter.submitList(data)
        tvEmpty.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mood_add, null)
        val emojiGrid = dialogView.findViewById<GridLayout>(R.id.emojiGrid)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)

        var selectedEmoji: String? = null

        // Make sure all children are TextViews (as in our XML)
        for (i in 0 until emojiGrid.childCount) {
            val child = emojiGrid.getChildAt(i)
            if (child is TextView) {
                child.alpha = 0.6f
                child.setOnClickListener {
                    for (j in 0 until emojiGrid.childCount) {
                        val c = emojiGrid.getChildAt(j)
                        if (c is TextView) c.alpha = 0.6f
                    }
                    child.alpha = 1f
                    selectedEmoji = child.text.toString()
                }
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_mood))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { d, _ ->
                selectedEmoji?.let { emoji ->
                    repo.add(emoji, etNote.text?.toString()?.trim().orEmpty())
                    refreshList()
                }
                d.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { d, _ -> d.dismiss() }
            .show()
    }
}
