package com.example.budgettracker.model

import androidx.lifecycle.*
import com.example.budgettracker.data.UsersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val usersDao: UsersDao): ViewModel() {

    private val _username = MutableLiveData<String>()

    private val _recoveryCode = MutableLiveData<String>()

    private val _password = MutableLiveData<String>()

    fun setUsername(userName: String){
        _username.value = userName
    }

    fun setRecoveryCode(recoveryCode: String){
        _recoveryCode.value = recoveryCode
    }

    fun setPassword(newPassword: String){
        _password.value = newPassword
    }

    fun validate(): LiveData<Boolean>{
        //check if a user with the entered username and recovery code exists
        return usersDao.userExistsByUserNameRecoveryCode(_username.value.toString(), _recoveryCode.value.toString()).asLiveData()
    }

    fun resetPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            val id = usersDao.getUserIdByNameCode(_username.value.toString(), _recoveryCode.value.toString())
            usersDao.update(_password.value.toString(), id)
        }
    }
}

class ForgotPasswordViewModelFactory(private val usersDao: UsersDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ForgotPasswordViewModel(usersDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}