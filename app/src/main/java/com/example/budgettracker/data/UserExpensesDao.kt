package com.example.budgettracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.entities.UserExpenses
import kotlinx.coroutines.flow.Flow

@Dao
interface UserExpensesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userExpenses: UserExpenses)

    @Query("UPDATE UserExpenses SET itm_id = :itemId, cost = :price WHERE user_expenses_id = :userExpenseId")
    suspend fun update(itemId: Int, price: Double, userExpenseId: Int)

    @Query("SELECT user_id FROM UserExpenses WHERE user_expenses_id = :userExpenseId")
    suspend fun getUserId(userExpenseId: Int): Int

    @Query("SELECT user_id FROM UserExpenses WHERE user_expenses_id = :userExpenseId")
    fun getUserId2(userExpenseId: Int): Flow<Int>

    // To delete a UserExpense from the database.
    @Query("DELETE FROM UserExpenses WHERE user_expenses_id = :userExpenseId")
    suspend fun delete(userExpenseId: Int)

    @Query("DELETE FROM UserExpenses WHERE user_id = :userId")
    suspend fun deleteAll(userId: Int)
}