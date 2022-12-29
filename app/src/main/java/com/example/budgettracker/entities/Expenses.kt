package com.example.budgettracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["item_name"], unique = true)])
data class Expenses (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId: Int = 0,
    @ColumnInfo(name = "item_name")
    val itemName: String
)