package com.example.assignment3.ui.mood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.domain.model.MoodEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoodAdapter(
    private var items: MutableList<MoodEntry>,
    private val onDelete: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.VH>() {

    fun submitList(newItems: MutableList<MoodEntry>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvEmoji: TextView = v.findViewById(R.id.tvEmoji)
        val tvNote: TextView = v.findViewById(R.id.tvNote)
        val tvWhen: TextView = v.findViewById(R.id.tvWhen)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDeleteMood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvEmoji.text = item.emoji
        holder.tvNote.text = item.note.ifBlank {
            holder.itemView.context.getString(R.string.mood_journal)
        }
        holder.tvWhen.text = formatWhen(item.timestampMillis)
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = items.size

    private fun formatWhen(ms: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(ms))
    }
}
