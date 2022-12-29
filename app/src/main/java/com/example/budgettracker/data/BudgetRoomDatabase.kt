package com.example.budgettracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgettracker.entities.Expenses
import com.example.budgettracker.entities.UserBudgetInfo
import com.example.budgettracker.entities.UserExpenses
import com.example.budgettracker.entities.Users

@Database(entities = [Users::class, UserBudgetInfo::class, Expenses::class,UserExpenses::class], version = 2, exportSchema = false)
abstract class BudgetRoomDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun userExpensesDao(): UserExpensesDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun userBudgetDao(): UserBudgetInfoDao

    companion object{
        @Volatile
        private var INSTANCE: BudgetRoomDatabase?=null
        fun getDatabase(context: Context): BudgetRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetRoomDatabase::class.java,
                    "budget_database"
                )
                    .fallbackToDestructiveMigration() // destroys and rebuilds the table when schema changes
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}