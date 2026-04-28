package com.example.taskmanagerapp.Adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.MVVM.Models.Task
import com.example.taskmanagerapp.databinding.RowTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClick: (Int) -> Unit,
    private val onTaskStatusChanged:(Int)->Unit
) : RecyclerView.Adapter<TaskAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val task = tasks[position]
        holder.binding.tvTaskTitle.text = task.title
        if (task.isDone) {
            holder.binding.tvTaskTitle.paintFlags = holder.binding.tvTaskTitle.paintFlags
            holder.binding.tvTaskTitle.alpha = 0.5f
            holder.binding.tvTaskSubtitle.text = "Marked as Done"
            holder.binding.tvTaskSubtitle.setTextColor(Color.GREEN)
        } else {
            holder.binding.tvTaskTitle.paintFlags = holder.binding.tvTaskTitle.paintFlags
            holder.binding.tvTaskTitle.alpha = 1.0f
            holder.binding.tvTaskSubtitle.text = "Tap to manage"
            holder.binding.tvTaskSubtitle.setTextColor(Color.GRAY)
        }
        holder.binding.ivDeleteTask.setOnClickListener {
            onDeleteClick(position)
        }
        holder.itemView.setOnClickListener {
            task.isDone = !task.isDone
            onTaskStatusChanged(position)
        }
    }

    override fun getItemCount() = tasks.size
    class VH(val binding: RowTaskBinding) : RecyclerView.ViewHolder(binding.root)
}