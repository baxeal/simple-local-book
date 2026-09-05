package com.local.simplebook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.local.simplebook.db.AppDatabase
import com.local.simplebook.db.BookRecord
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(db: AppDatabase, onBack:()->Unit){
    var list by remember { mutableStateOf(listOf<BookRecord>()) }
    var totalIncome by remember { mutableStateOf(0.0) }
    var totalPay by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    fun calc(){
        scope.launch {
            val all = db.bookRecordDao().getAllRecords()
            list = all
            totalIncome = all.filter { it.isIncome }.sumOf { it.amount }
            totalPay = all.filter { !it.isIncome }.sumOf { it.amount }
        }
    }

    LaunchedEffect(Unit){ calc() }

    Scaffold(topBar = {
        TopAppBar(title={Text("统计看板")}, navigationIcon={
            TextButton(onClick = onBack){Text("返回")}
        })
    }) {padding->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(20.dp)) {
            Card(modifier = Modifier.fillMaxWidth()){
                Column(modifier = Modifier.padding(16.dp)){
                    Text("总收入：$totalIncome")
                    Text("总支出：$totalPay")
                    Text("结余：${totalIncome‑totalPay}")
                }
            }
        }
    }
}
