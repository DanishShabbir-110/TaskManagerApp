package com.example.taskmanagerapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val binding: FragmentDashboardBinding by lazy{
        FragmentDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigation.selectedItemId = R.id.nav_tasks
        if (savedInstanceState == null) {
            loadFragment(TaskFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_counter -> {
                    loadFragment(CounterFragment())
                    true
                }
                R.id.nav_tasks -> {
                    loadFragment(TaskFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.dashboard_container, fragment)
            .commit()
    }
}