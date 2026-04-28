package com.example.taskmanagerapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagerapp.Adapters.CounterAdapter
import com.example.taskmanagerapp.MVVM.Models.Counter
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.databinding.FragmentCounterBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CounterFragment : Fragment() {
    private val gson = Gson()
    private var selectedCounterPosition: Int = -1
    private var currentCount = 0
    private var savedCounters = mutableListOf<Counter>()
    private lateinit var counterAdapter: CounterAdapter
    private val binding: FragmentCounterBinding by lazy{
        FragmentCounterBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        binding.btnPlus.setOnClickListener {
            currentCount++
            binding.tvCount.text = currentCount.toString()
        }

        binding.btnMinus.setOnClickListener {
            if (currentCount > 0) currentCount--
            binding.tvCount.text = currentCount.toString()
        }

        binding.btnReset.setOnClickListener {
            currentCount = 0
            binding.tvCount.text = "0"
        }

        binding.btnSaveCounter.setOnClickListener {
            showSaveDialog()
        }

        setupRecyclerView()
    }
    private fun showSaveDialog() {
        val input = EditText(requireContext())
        if (selectedCounterPosition != -1) {
            input.setText(savedCounters[selectedCounterPosition].title)
        } else {
            input.hint = "e.g. SubhanAllah or Steps"
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (selectedCounterPosition != -1) "Update Counter" else "Save Counter")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val title = input.text.toString()
                if (title.isNotEmpty()) {
                    if (selectedCounterPosition != -1) {
                        savedCounters[selectedCounterPosition].value = currentCount
                        savedCounters[selectedCounterPosition] = Counter(title, currentCount)
                        Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        savedCounters.add(Counter(title, currentCount))
                    }

                    saveData()
                    updateList()
                    selectedCounterPosition = -1
                }
            }.show()
    }

    private fun setupRecyclerView() {
        counterAdapter = CounterAdapter(
            savedCounters,
            onItemClick = { clickedCounter, position ->
                currentCount = clickedCounter.value
                selectedCounterPosition = position
                binding.tvCount.text = currentCount.toString()
                Toast.makeText(requireContext(), "${clickedCounter.title} Loaded", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { position ->
                savedCounters.removeAt(position)
                if (selectedCounterPosition == position) selectedCounterPosition = -1
                saveData()
                counterAdapter.notifyItemRemoved(position)
                counterAdapter.notifyItemRangeChanged(position, savedCounters.size)
            }
        )
        binding.rvSavedCounters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSavedCounters.adapter = counterAdapter
    }

    private fun updateList() {
        counterAdapter.notifyDataSetChanged()
    }

    private fun saveData() {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("SAVED_COUNTERS", gson.toJson(savedCounters)).apply()
        prefs.edit().putInt("CURRENT_COUNT", currentCount).apply()
    }

    private fun loadData() {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("SAVED_COUNTERS", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Counter>>() {}.type
            savedCounters = gson.fromJson(json, type)
        }
        currentCount = prefs.getInt("CURRENT_COUNT", 0)
        binding.tvCount.text = currentCount.toString()
    }
}