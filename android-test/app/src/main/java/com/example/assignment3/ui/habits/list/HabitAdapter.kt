package com.example.assignment3.ui.habits.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.domain.model.Habit

class HabitAdapter(
    private var items: MutableList<Habit>,
    private val isDone: (String) -> Boolean,
    private val onToggle: (Habit) -> Unit,
    private val onDelete: (Habit) -> Unit,
    private val onEdit: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.VH>() {

    fun submitList(newItems: MutableList<Habit>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val cbDone: CheckBox = v.findViewById(R.id.cbDone)
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = v.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit_row, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = isDone(item.id)
        holder.cbDone.setOnCheckedChangeListener { _, _ -> onToggle(item) }
        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = items.size
}
