package com.example.budgettracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Users (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val password: String,
    @ColumnInfo(name = "recovery_code")
    val recoveryCode: String,
)