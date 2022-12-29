package com.example.budgettracker.model

import androidx.lifecycle.*
import com.example.budgettracker.data.UsersDao

class LoginViewModel(private val usersDao: UsersDao): ViewModel() {

    fun checkUserExists(username: String, password: String): LiveData<Boolean> {
        return usersDao.userExists(username, password).asLiveData()
    }

    fun getUserId(username: String): LiveData<Int> {
        return usersDao.getUserIdByName(username).asLiveData()
    }
}

class LoginViewModelFactory(private val usersDao: UsersDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usersDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}