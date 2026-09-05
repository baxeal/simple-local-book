package com.local.simplebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.local.simplebook.db.AppDatabase
import com.local.simplebook.db.Category
import com.local.simplebook.ui.BoardScreen
import com.local.simplebook.ui.CategoryManageScreen
import com.local.simplebook.ui.HomeScreen
import com.local.simplebook.ui.RecordListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "simple_book_db"
        ).build()

        //初始化默认分类
        CoroutineScope(Dispatchers.IO).launch {
            if(db.categoryDao().getCategories(true).isEmpty()){
                db.categoryDao().insertCategory(Category(name="工资", isIncome = true))
                db.categoryDao().insertCategory(Category(name="兼职", isIncome = true))
            }
            if(db.categoryDao().getCategories(false).isEmpty()){
                db.categoryDao().insertCategory(Category(name="餐饮", isIncome = false))
                db.categoryDao().insertCategory(Category(name="交通", isIncome = false))
                db.categoryDao().insertCategory(Category(name="购物", isIncome = false))
            }
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var pageId by remember { mutableStateOf("home") }
                    when(pageId){
                        "home" -> HomeScreen(db,
                            onGoList = {pageId="list"},
                            onGoCategory = {pageId="category"}
                        )
                        "list" -> RecordListScreen(db, onBack = {pageId="home"})
                        "category" -> CategoryManageScreen(db, onBack = {pageId="home"})
                        "board" -> BoardScreen(db, onBack = {pageId="home"})
                    }
                }
            }
        }
    }
}
