package com.example.budgettracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.entities.UserBudgetInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBudgetInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userBudget: UserBudgetInfo)

    @Query("UPDATE UserBudgetInfo SET budget = :userBudget WHERE user_id = :userId")
    suspend fun updateBudget(userBudget: Double, userId: Int)

    @Query("UPDATE UserBudgetInfo SET money_spent = :moneySpent WHERE user_id = :userId")
    suspend fun updateMoneySpent(moneySpent: Double, userId: Int)

    @Query("SELECT budget FROM UserBudgetInfo WHERE user_id = :userId")
    fun getBudget(userId: Int): Flow<Double>

    @Query("SELECT money_spent FROM UserBudgetInfo WHERE user_id = :userId")
    fun getMoneySpent(userId: Int): Flow<Double>

    @Query("SELECT money_spent FROM UserBudgetInfo WHERE user_id = :userId")
    suspend fun getMoneySpent2(userId: Int): Double
}