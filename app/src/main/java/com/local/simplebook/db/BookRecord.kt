package com.local.simplebook.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_record")
data class BookRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val remark: String,
    val categoryId: Long,
    val timestamp: Long,
    val isIncome: Boolean
)
