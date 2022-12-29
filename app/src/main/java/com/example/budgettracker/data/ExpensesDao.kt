package com.example.budgettracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.entities.Expenses
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expenses)

    @Query("SELECT item_id FROM Expenses WHERE item_name = :itemName")
    suspend fun getItem(itemName: String): Int?

    @Query("SELECT * FROM Expenses INNER JOIN UserExpenses ON Expenses.item_id = UserExpenses.itm_id WHERE user_id = :userId")
    fun getAllUserAndExpenses(userId: Int): Flow<List<ExpensesAndUserExpenses>>

    @Query("SELECT * FROM Expenses INNER JOIN UserExpenses ON Expenses.item_id = UserExpenses.itm_id WHERE user_expenses_id = :userExpenseId")
    fun getUserAndExpenses(userExpenseId: Int): Flow<ExpensesAndUserExpenses>
}