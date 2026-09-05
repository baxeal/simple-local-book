package com.local.simplebook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.local.simplebook.db.AppDatabase
import com.local.simplebook.db.BookRecord
import com.local.simplebook.db.Category
import com.local.simplebook.utils.NaturalTextParser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(db: AppDatabase, onGoList:()->Unit, onGoCategory:()->Unit){
    var inputText by remember { mutableStateOf("") }
    var incomeCats by remember { mutableStateOf(listOf<Category>()) }
    var expendCats by remember { mutableStateOf(listOf<Category>()) }
    var selCat:Category? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        incomeCats = db.categoryDao().getCategories(true)
        expendCats = db.categoryDao().getCategories(false)
    }

    Scaffold(
        topBar = { TopAppBar(title={Text("简易记账")}) }
    ) { padding->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            TextField(
                value = inputText,
                onValueChange = {inputText=it},
                label = {Text("输入，例：+200工资 / -50吃饭")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                val res = NaturalTextParser.parse(inputText)
                res?.let {parsed->
                    val targetCats = if(parsed.isIncome) incomeCats else expendCats
                    if(targetCats.isNotEmpty()){
                        val cat = selCat ?: targetCats.first()
                        val rec = BookRecord(
                            amount = parsed.amount,
                            remark = parsed.remark,
                            categoryId = cat.id,
                            timestamp = System.currentTimeMillis(),
                            isIncome = parsed.isIncome
                        )
                        scope.launch {
                            db.bookRecordDao().insertRecord(rec)
                            inputText=""
                        }
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("保存记录")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                Button(onClick = onGoList){ Text("全部记录") }
                Button(onClick = onGoCategory){ Text("分类管理") }
            }
        }
    }
}
