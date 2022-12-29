package com.example.budgettracker.entities

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(
    entity = Users::class,
    childColumns = ["user_id"],
    parentColumns = ["id"]
)], indices = [Index(value = ["user_id"], unique = true)])
data class UserBudgetInfo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val budgetId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val budget: Double,
    @ColumnInfo(name = "money_spent")
    val moneySpent: Double
)