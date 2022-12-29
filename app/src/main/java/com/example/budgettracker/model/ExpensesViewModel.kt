package com.example.budgettracker.model

import androidx.lifecycle.*
import com.example.budgettracker.data.ExpensesAndUserExpenses
import com.example.budgettracker.data.ExpensesDao
import com.example.budgettracker.data.UserBudgetInfoDao
import com.example.budgettracker.data.UserExpensesDao
import com.example.budgettracker.entities.Expenses
import com.example.budgettracker.entities.UserExpenses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpensesViewModel(private val userBudgetInfo: UserBudgetInfoDao,
                        private val expensesDao: ExpensesDao,
                        private val userExpensesDao: UserExpensesDao): ViewModel() {

    private val _moneySpent = MutableLiveData<Double>()
    val moneySpent: LiveData<Double> = _moneySpent
    private val _budget = MutableLiveData<Double>() //For budget entered by the user
    val budget: LiveData<Double> = _budget


    fun retrieveAllUserExpenses(userId: Int): LiveData<List<ExpensesAndUserExpenses>> {
        return expensesDao.getAllUserAndExpenses(userId).asLiveData()
    }

    fun retrieveUserExpenses(userExpenseId: Int): LiveData<ExpensesAndUserExpenses> {
        return expensesDao.getUserAndExpenses(userExpenseId).asLiveData()
    }

    private fun insertUserExpense(userExpense: UserExpenses){
        viewModelScope.launch(Dispatchers.IO) {
            userExpensesDao.insert(userExpense)
        }
    }

    fun deleteUserExpense(userId: Int, userExpenseId: Int, price: Double) {
        viewModelScope.launch(Dispatchers.IO){
            val moneySpent = userBudgetInfo.getMoneySpent2(userId)
            userBudgetInfo.updateMoneySpent(moneySpent - price, userId) //update the money_spent in the UserBudgetInfo Table
            userExpensesDao.delete(userExpenseId)
        }
    }

    fun deleteAllUserExpenses(userId: Int) {
        viewModelScope.launch(Dispatchers.IO){
            userBudgetInfo.updateMoneySpent(0.0, userId) //update the money_spent in the UserBudgetInfo Table
            userExpensesDao.deleteAll(userId)
        }
    }

    fun addEditItem(userId: Int, userExpenseId: Int, itemName: String, priceOld: Double, priceNew: Double, dateTime: String){
        viewModelScope.launch(Dispatchers.IO){
            var itemId = expensesDao.getItem(itemName.uppercase()) //getting item_id if item exists in Expenses Table

            /* if the item is not present in the Expenses table then insert it, then get the item_id of the inserted item */
            if (itemId == null){
                val expense = Expenses(itemName = itemName.uppercase())
                expensesDao.insert(expense)
                itemId = expensesDao.getItem(itemName.uppercase())
            }

            val moneySpent = userBudgetInfo.getMoneySpent2(userId)
            val newPrice = priceNew - priceOld
            userBudgetInfo.updateMoneySpent(moneySpent + newPrice, userId) //update the money_spent in the UserBudgetInfo Table

            if (userExpenseId < 0){
                val userExpense = UserExpenses(userId = userId, itmId = itemId!!, cost = priceNew, dateTime = dateTime)
                insertUserExpense(userExpense)
            }
            else{
                userExpensesDao.update(itemId = itemId!!, price = priceNew, userExpenseId = userExpenseId)
            }
        }
    }

    //gets the value of money_spent from table
    fun getMoneySpent(userId: Int): LiveData<Double>{
        return userBudgetInfo.getMoneySpent(userId).asLiveData()
    }

    fun setMoneySpent(moneySpent: Double){
        _moneySpent.value = moneySpent
    }

    fun setBudget(userBudget: String){
        _budget.value = userBudget.toDouble()
    }

    //gets the value of budget from table to display it as TextView
    fun getBudget(userId: Int): LiveData<Double>{
        return userBudgetInfo.getBudget(userId).asLiveData()
    }

    fun updateBudget(userId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _budget.value?.let { userBudgetInfo.updateBudget(it, userId) } // userBudgetInfo.updateBudget(_budget.value, userId)
        }
    }
}

class ExpensesViewModelFactory(private val userBudgetInfoDao: UserBudgetInfoDao,
                               private val expensesDao: ExpensesDao,
                               private val userExpensesDao: UserExpensesDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(userBudgetInfoDao, expensesDao, userExpensesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}