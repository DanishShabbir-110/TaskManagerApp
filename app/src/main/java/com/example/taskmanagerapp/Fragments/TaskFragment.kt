package com.example.taskmanagerapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagerapp.Adapters.TaskAdapter
import com.example.taskmanagerapp.MVVM.Models.Task
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.databinding.FragmentTaskBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskFragment : Fragment() {
    private val binding: FragmentTaskBinding by lazy {
        FragmentTaskBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()
    private val gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTasksFromPrefs()
        adapter = TaskAdapter(
            taskList,
            onDeleteClick ={ position ->
                taskList.removeAt(position)
                saveTasksToPrefs()
                adapter.notifyItemRemoved(position)
            },
            onTaskStatusChanged = { position ->
                saveTasksToPrefs()
                adapter.notifyItemChanged(position)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }
    private fun showAddTaskDialog() {
        val editText = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("Add New Task")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val taskTitle = editText.text.toString()
                if (taskTitle.isNotEmpty()) {
                    taskList.add(Task(taskTitle))
                    saveTasksToPrefs()
                    adapter.notifyItemInserted(taskList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveTasksToPrefs() {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val json = gson.toJson(taskList)
        prefs.edit().putString("TASK_LIST", json).apply()
    }

    private fun loadTasksFromPrefs() {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("TASK_LIST", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            taskList = gson.fromJson(json, type)
        }
    }
}