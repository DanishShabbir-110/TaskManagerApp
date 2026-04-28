package com.example.taskmanagerapp.MVVM.ViewModelsFactory

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagerapp.MVVM.ViewModels.AuthViewModel

class AuthViewModelFactory(private val sharedPrefs: SharedPreferences): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(sharedPrefs)as T
        }
        throw IllegalArgumentException("View Model Class never find.")
    }
}