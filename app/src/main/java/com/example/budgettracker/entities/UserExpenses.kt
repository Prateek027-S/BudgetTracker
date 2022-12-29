package com.example.budgettracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Expenses::class,
    childColumns = ["itm_id"],
    parentColumns = ["item_id"]
)])
data class UserExpenses (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_expenses_id")
    val userExpensesId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "itm_id")
    val itmId: Int,
    val cost: Double,
    val dateTime: String
)
// fun UserExpenses.getFormattedCost(): String = NumberFormat.getCurrencyInstance().format(cost)