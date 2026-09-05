package com.local.simplebook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.dao

@Database(
    entities = [BookRecord::class, Category::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookRecordDao(): BookRecordDao
    abstract fun categoryDao(): CategoryDao
}

@dao
interface BookRecordDao {
    @androidx.room.Query("SELECT * FROM book_record ORDER BY timestamp DESC")
    suspend fun getAllRecords(): List<BookRecord>

    @androidx.room.Insert
    suspend fun insertRecord(item: BookRecord)

    @androidx.room.Update
    suspend fun updateRecord(item: BookRecord)

    @androidx.room.Delete
    suspend fun deleteRecord(item: BookRecord)
}

@dao
interface CategoryDao {
    @androidx.room.Query("SELECT * FROM category WHERE isIncome = :isIncome")
    suspend fun getCategories(isIncome: Boolean): List<Category>

    @androidx.room.Insert
    suspend fun insertCategory(item: Category)

    @androidx.room.Delete
    suspend fun deleteCategory(item: Category)
}
