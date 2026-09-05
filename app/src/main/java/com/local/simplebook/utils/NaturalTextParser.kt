package com.local.simplebook.utils

data class ParseResult(
    val amount: Double,
    val remark: String,
    val isIncome: Boolean
)

object NaturalTextParser {
    fun parse(text: String): ParseResult? {
        val numRegex = Regex("""([+-]?\d+\.?\d*)""")
        val match = numRegex.find(text) ?: return null
        val numStr = match.groupValues[1]
        val amount = numStr.toDoubleOrNull() ?: return null
        val remark = text.replace(numRegex,"").trim()
        val isIncome = numStr.startsWith("+")
        return ParseResult(amount, remark, isIncome)
    }
}
