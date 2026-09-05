package com.local.simplebook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.local.simplebook.db.AppDatabase
import com.local.simplebook.db.BookRecord
import com.local.simplebook.utils.CsvExporter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordListScreen(db: AppDatabase, onBack: () -> Unit) {
    var records by remember { mutableStateOf(listOf<BookRecord>()) }
    var editItem: BookRecord? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val fmt = SimpleDateFormat("yyyy‑MM‑dd HH:mm", Locale.CHINA)

    fun load() {
        scope.launch {
            records = db.bookRecordDao().getAllRecords()
        }
    }

    LaunchedEffect(Unit) { load() }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("全部记账记录") },
            navigationIcon = { TextButton(onClick = onBack) { Text("返回") } }
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(onClick = {
                val csvText = CsvExporter.toCsv(records)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("导出CSV")
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(records) { rec ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                        onClick = { editItem = rec }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = fmt.format(Date(rec.timestamp)))
                            Text(
                                text = if (rec.isIncome) "收入 ${rec.amount}" else "支出 ${rec.amount}",
                                color = if (rec.isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            Text(text = rec.remark)
                        }
                    }
                }
            }
        }
    }

    EditRecordDialog(
        record = editItem,
        onDismiss = { editItem = null },
        onSave = {
            scope.launch { db.bookRecordDao().updateRecord(it) }
            editItem = null
            load()
        },
        onDelete = {
            scope.launch { db.bookRecordDao().deleteRecord(it) }
            editItem = null
            load()
        }
    )
}
