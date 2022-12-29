package com.example.budgettracker.model

import androidx.lifecycle.*
import com.example.budgettracker.data.UserBudgetInfoDao
import com.example.budgettracker.data.UsersDao
import com.example.budgettracker.entities.UserBudgetInfo
import com.example.budgettracker.entities.Users
import kotlinx.coroutines.launch

class SignUpViewModel(private val usersDao: UsersDao, private val userBudgetDao: UserBudgetInfoDao): ViewModel() {
    private val _username = MutableLiveData<String>()

    private val _recoveryCode = MutableLiveData<String>()
    val recoveryCode: LiveData<String> = _recoveryCode

    private val _password = MutableLiveData<String>()

    var retain = false
    var newAccount = false

    private fun insertUser(user: Users){
        viewModelScope.launch {
            usersDao.insert(user)
            val userId = usersDao.getLastUser()
            userBudgetDao.insert(getNewUserBudgetEntry(userId))
        }
    }

    private fun getNewUserEntry(): Users {
        return Users(
            name = _username.value.toString(),
            password = _password.value.toString(),
            recoveryCode = _recoveryCode.value.toString()
        )
    }

    private fun getNewUserBudgetEntry(userId: Int): UserBudgetInfo{
        return UserBudgetInfo(
            userId = userId,
            budget = 0.0,
            moneySpent = 0.0
        )
    }

    fun checkUsernameExists(username: String): LiveData<Boolean> {
        return usersDao.usernameExists(username).asLiveData()
    }

    fun addNewUser() {
        val newUser = getNewUserEntry()
        insertUser(newUser)
    }

    fun areEntriesValid(password: String, confirmPassword: String): Int{
        var chk = 0
        if (password.isBlank()){
            chk = 1
        }
        else if (password != confirmPassword){
            chk = 2
        }
        else if (password == confirmPassword){
            chk = 3
        }
        return chk
    }

    fun setUsername(userName: String){
        _username.value = userName
    }

    fun setRecoveryCode(){
        if (retain == false) {
            _recoveryCode.value = (100000..999999).random().toString()
        }
    }

    fun setPassword(newPassword: String){
        _password.value = newPassword
    }
}

class SignUpViewModelFactory(private val usersDao: UsersDao, private val userBudgetDao: UserBudgetInfoDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(usersDao, userBudgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}