package com.example.taskmanagerapp.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerapp.MVVM.Models.User
import com.example.taskmanagerapp.MVVM.ViewModels.AuthViewModel
import com.example.taskmanagerapp.MVVM.ViewModelsFactory.AuthViewModelFactory
import com.example.taskmanagerapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private val factory by lazy {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        AuthViewModelFactory(prefs)
    }
    private val binding: FragmentRegisterBinding by lazy {
        FragmentRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: AuthViewModel by viewModels { factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            validateAndRegister()
        }
        binding.tvBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateAndRegister() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        when {
            name.isEmpty() -> binding.etName.error = "Please enter your name"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> binding.etEmail.error =
                "Invalid email format"

            password.length < 6 -> binding.etPassword.error =
                "Password must be at least 6 characters"

            else -> {
                viewModel.register(User(email, password, name))
                Toast.makeText(
                    requireContext(),
                    "Account Created Successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }

}