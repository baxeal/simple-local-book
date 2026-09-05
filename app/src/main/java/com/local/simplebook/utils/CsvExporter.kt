package com.local.simplebook.utils

import com.local.simplebook.db.BookRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {
    fun toCsv(records: List<BookRecord>): String {
        val sb = StringBuilder()
        sb.append("时间,金额,收支,备注\n")
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        for(r in records){
            val timeStr = fmt.format(Date(r.timestamp))
            val typeStr = if(r.isIncome) "收入" else "支出"
            sb.append("\"$timeStr\",\"${r.amount}\",\"$typeStr\",\"${r.remark}\"\n")
        }
        return sb.toString()
    }
}
