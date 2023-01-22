package com.eywa.projectnummi.features.transactionsSummary.state

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.model.HasNameAndId
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.utils.ColorUtils

enum class TransactionsSummaryGrouping(private val displayName: String) : HasNameAndId {
    CATEGORY("Category") {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .flatMap { transaction ->
                            transaction.amounts.map { it.category to (it.amount * transaction.getAmountMultiplier()) }
                        }
                        .groupBy { it.first }
                        .map { (category, amounts) ->
                            TransactionsSummaryPieItem(
                                    category?.name,
                                    category?.displayColor,
                                    amounts.sumOf { it.second },
                            )
                        }
    },
    PERSON("Person") {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .flatMap { transaction ->
                            transaction.amounts.map { it.person to (it.amount * transaction.getAmountMultiplier()) }
                        }
                        .groupBy { it.first }
                        .map { (person, amounts) -> person?.name to amounts.sumOf { it.second } }
                        .addColours()
    },
    ACCOUNT("Account") {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .groupBy { it.account }
                        .map { (person, transactions) ->
                            person?.name to transactions.sumOf { transaction ->
                                transaction.amounts.sumOf { it.amount } * transaction.getAmountMultiplier()
                            }
                        }
                        .addColours()
    },
    OUTGOING("Incoming/Outgoing") {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .map { transaction -> transaction.isOutgoing to transaction.amounts.sumOf { it.amount } }
                        .groupBy { it.first }
                        .mapValues { (_, amounts) -> amounts.sumOf { it.second } }
                        .let {
                            listOf(
                                    TransactionsSummaryPieItem(
                                            INCOMING_NAME,
                                            Color.Transparent,
                                            it[false] ?: 0,
                                    ),
                                    TransactionsSummaryPieItem(
                                            OUTGOING_NAME,
                                            Color.Transparent,
                                            it[true] ?: 0,
                                    )
                            )
                        }
    }
    ;

    abstract fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem>

    override fun getItemId(): Int = ordinal
    override fun getItemName(): String = displayName

    fun List<Pair<String?, Int>>.addColours(): List<TransactionsSummaryPieItem> {
        if (isEmpty()) return emptyList()

        val nonNullCount = count { it.first != null && it.second > 0 }
        val increment = 1f / (nonNullCount)
        val defaultColors = listOf(
                1f, // Red
                0.5f, // Light blue
                0.15f, // Yellow
                0.72f, // Purple
                0.32f, // Green
                0.66f, // Dark blue
                0.9f, // Pink
        )
        val colors =
                if (nonNullCount < defaultColors.size) defaultColors.take(nonNullCount)
                else List(nonNullCount) { it * increment }
        val alphabeticalOrder = filter { it.first != null && it.second > 0 }.map { it.first }.sortedBy { it!! }
                .mapIndexed { index, i -> i to index }.toMap()

        return map { (name, amount) ->
            val color =
                    if (name == null) null
                    else if (amount <= 0) Color.Transparent
                    else ColorUtils.asCategoryColor(colors[alphabeticalOrder[name]!!])
            TransactionsSummaryPieItem(
                    name,
                    color,
                    amount,
            )
        }
    }


    companion object {
        const val INCOMING_NAME = "NUMMI_PIE_ITEM_INCOMING"
        const val OUTGOING_NAME = "NUMMI_PIE_ITEM_OUTGOING"

        private fun Transaction.getAmountMultiplier() = if (isOutgoing) 1 else (-1)
    }
}
