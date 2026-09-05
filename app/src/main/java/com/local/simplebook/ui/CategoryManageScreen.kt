package com.local.simplebook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.local.simplebook.db.AppDatabase
import com.local.simplebook.db.Category
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManageScreen(db: AppDatabase, onBack:()->Unit){
    var incomeList by remember { mutableStateOf(listOf<Category>()) }
    var expendList by remember { mutableStateOf(listOf<Category>()) }
    var newCatName by remember { mutableStateOf("") }
    var isIncomeMode by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun reload(){
        scope.launch {
            incomeList = db.categoryDao().getCategories(true)
            expendList = db.categoryDao().getCategories(false)
        }
    }

    LaunchedEffect(Unit){ reload() }

    Scaffold(topBar = {
        TopAppBar(title={Text("分类管理")}, navigationIcon={
            TextButton(onClick = onBack){Text("返回")}
        })
    }) {padding->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            Row {
                Switch(checked = isIncomeMode, onCheckedChange = {isIncomeMode=it})
                Text(if(isIncomeMode)"新增收入分类" else "新增支出分类")
            }

            Row(modifier = Modifier.fillMaxWidth()){
                TextField(
                    value = newCatName,
                    onValueChange = {newCatName=it},
                    modifier = Modifier.weight(1f),
                    label = {Text("分类名称")}
                )
                Button(onClick = {
                    if(newCatName.isNotBlank()){
                        scope.launch {
                            db.categoryDao().insertCategory(Category(name=newCatName.trim(), isIncome = isIncomeMode))
                            newCatName=""
                            reload()
                        }
                    }
                }){ Text("添加") }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("收入分类", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.height(120.dp)){
                items(incomeList){cat->
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text(cat.name, modifier=Modifier.weight(1f))
                        TextButton(onClick={
                            scope.launch { db.categoryDao().deleteCategory(cat); reload() }
                        }){ Text("删除") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("支出分类", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.height(120.dp)){
                items(expendList){cat->
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text(cat.name, modifier=Modifier.weight(1f))
                        TextButton(onClick={
                            scope.launch { db.categoryDao().deleteCategory(cat); reload() }
                        }){ Text("删除") }
                    }
                }
            }
        }
    }
}
