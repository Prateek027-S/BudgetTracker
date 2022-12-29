package com.example.budgettracker.data

import androidx.room.ColumnInfo
import java.text.NumberFormat

data class ExpensesAndUserExpenses(
    @ColumnInfo(name = "user_expenses_id") val userExpensesId: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int = 0,
    @ColumnInfo(name = "item_id") val itemId: Int = 0,
    @ColumnInfo(name = "itm_id") val itmId: Int = 0,
    @ColumnInfo(name = "item_name") val itemName: String = "",
    val cost: Double = 0.0,
    val dateTime: String = ""
)
fun ExpensesAndUserExpenses.getFormattedCost(): String = NumberFormat.getCurrencyInstance().format(cost)
