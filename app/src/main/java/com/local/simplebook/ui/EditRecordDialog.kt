package com.local.simplebook.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.local.simplebook.db.BookRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecordDialog(
    record: BookRecord?,
    onDismiss: ()->Unit,
    onSave:(BookRecord)->Unit,
    onDelete:(BookRecord)->Unit
){
    if(record == null) return
    var amountStr by remember { mutableStateOf(record.amount.toString()) }
    var remarkStr by remember { mutableStateOf(record.remark) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑记录") },
        text = {
            Column {
                TextField(
                    value = amountStr,
                    onValueChange = {amountStr=it},
                    label = {Text("金额")},
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = remarkStr,
                    onValueChange = {remarkStr=it},
                    label = {Text("备注")},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amt = amountStr.toDoubleOrNull()
                amt?.let {
                    val updated = record.copy(amount = it, remark = remarkStr)
                    onSave(updated)
                }
            }) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = { onDelete(record) }) { Text("删除") }
        }
    )
}
