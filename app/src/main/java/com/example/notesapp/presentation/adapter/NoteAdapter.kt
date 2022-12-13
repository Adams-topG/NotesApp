package com.example.notesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.data.models.NoteModel
import com.example.notesapp.databinding.RvItemBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(val onClick: (note: NoteModel) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var listNote = emptyList<NoteModel>()

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RvItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val listPosition = listNote[position]
        val timeStamp = listPosition.date
        val seconds = System.currentTimeMillis() - timeStamp
        val visibilityDate = (seconds / 1000) / 60 > 1440
        val currentDate = convertLongToDate(timeStamp)
        val currentTime = convertLongToTime(timeStamp)

        with(holder) {
            binding.clItem.setOnClickListener {
                onClick(listPosition)
            }
            if (visibilityDate) {
                binding.tvDate.text = currentDate
                binding.tvTitle.text = listPosition.title
                binding.tvDesc.text = listPosition.description
                binding.tvTime.text = currentTime
            } else {
                binding.tvDate.text = itemView.context.getString(R.string.today_item_text)
                binding.tvTitle.text = listPosition.title
                binding.tvDesc.text = listPosition.description
                binding.tvTime.text = currentTime
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    fun setList(list: List<NoteModel>) {
        listNote = list
        notifyDataSetChanged()
    }

    override fun onViewDetachedFromWindow(holder: NoteViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("d. MMMM")
        return format.format(date)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}