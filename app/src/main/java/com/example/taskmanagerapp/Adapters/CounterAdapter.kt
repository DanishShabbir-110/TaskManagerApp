package com.example.taskmanagerapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.MVVM.Models.Counter
import com.example.taskmanagerapp.databinding.RowTaskBinding

class CounterAdapter (
    private val counters: List<Counter>,
    private val onItemClick: (Counter,Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<CounterAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val counter = counters[position]
        holder.binding.tvTaskTitle.text = "${counter.title}: ${counter.value}"

        holder.itemView.setOnClickListener {
            onItemClick(counter,position)
        }
        holder.binding.ivDeleteTask.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount() = counters.size
}