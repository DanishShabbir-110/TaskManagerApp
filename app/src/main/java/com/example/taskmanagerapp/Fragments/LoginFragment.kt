package com.example.taskmanagerapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.ContentInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerapp.MVVM.ViewModels.AuthViewModel
import com.example.taskmanagerapp.MVVM.ViewModelsFactory.AuthViewModelFactory
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.databinding.DialogForgotPasswordBinding
import com.example.taskmanagerapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val factory by lazy {
        val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        AuthViewModelFactory(prefs)
    }

    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: AuthViewModel by viewModels{factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loginStatus.observe(viewLifecycleOwner){isSuccess->
            if(isSuccess){
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()

                findNavController().navigate(R.id.dashboardFragment, null, navOptions)
            }else{
                Toast.makeText(requireContext(), "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email = email, password = password)
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }
    private fun showForgotPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val bind= DialogForgotPasswordBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        bind.btnDialogReset.setOnClickListener {
            val email = bind.etDialogEmail.text.toString().trim()
            val newPass = bind.etDialogNewPass.text.toString().trim()

            if (email.isNotEmpty() && newPass.length >= 6) {
                val isSuccess = viewModel.resetPassword(email, newPass)
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Password Updated!", Toast.LENGTH_SHORT).show()
                    builder.dismiss()
                } else {
                    bind.etDialogEmail.error = "Email not found!"
                }
            } else {
                Toast.makeText(requireContext(), "Enter valid details", Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()
    }
}