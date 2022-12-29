package com.example.budgettracker

import android.app.Application
import com.example.budgettracker.data.BudgetRoomDatabase

class BudgetApplication: Application() {
    val database: BudgetRoomDatabase by lazy { BudgetRoomDatabase.getDatabase(this) }
}