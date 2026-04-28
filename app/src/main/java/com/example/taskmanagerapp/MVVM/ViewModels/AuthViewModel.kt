package com.example.taskmanagerapp.MVVM.ViewModels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanagerapp.MVVM.Models.User
import com.google.gson.Gson

class AuthViewModel(private val sharedPrefs: SharedPreferences) : ViewModel() {
    private val isLogin = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = isLogin

    private val gson = Gson()
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(email: String, password: String) {
        val user = sharedPrefs.getString("User-Data", null)
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Please fill all Fields.."
            return
        }
        if (user != null) {
            val savedUser = gson.fromJson(user, User::class.java)
            if (savedUser.email == email && savedUser.password == password) {
                isLogin.value = true
                _errorMessage.value = null
            } else {
                _errorMessage.value = "Invalid Credentails"
                isLogin.value = false
            }
        }
    }


    fun register(registerUser: User) {
        val userJson = sharedPrefs.getString("User-Data", null)
        if (userJson != null) {
            val savedUser = gson.fromJson(userJson, User::class.java)
            if (savedUser != null && savedUser.email == registerUser.email) {
                _errorMessage.value = "User Already Registered!!"
                return
            }
        }
        val userString = gson.toJson(registerUser)
        sharedPrefs.edit().putString("User-Data", userString).apply()
    }
    fun resetPassword(email: String, newPass: String): Boolean {
        val userJson = sharedPrefs.getString("USER_DATA", null) ?: return false
        val user = gson.fromJson(userJson, User::class.java)

        if (user.email == email) {
            val updatedUser = user.copy(password = newPass)
            register(updatedUser)
            return true
        }
        return false
    }
}