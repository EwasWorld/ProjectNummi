package com.eywa.projectnummi.features.transactionsSummary.state

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.utils.ColorUtils

enum class TransactionsSummaryGrouping {
    CATEGORY {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .flatMap { transaction ->
                            transaction.amounts.map { it.category to (it.amount * transaction.getAmountMultiplier()) }
                        }
                        .groupBy { it.first }
                        .map { (category, amounts) ->
                            TransactionsSummaryPieItem(
                                    category?.name,
                                    category?.color,
                                    amounts.sumOf { it.second },
                            )
                        }
    },
    PERSON {
        override fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem> =
                (transactions ?: emptyList())
                        .flatMap { transaction ->
                            transaction.amounts.map { it.person to (it.amount * transaction.getAmountMultiplier()) }
                        }
                        .groupBy { it.first }
                        .map { (person, amounts) -> person?.name to amounts.sumOf { it.second } }
                        .addColours()
    },
    ACCOUNT {
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
    OUTGOING {
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
                                            it[false] ?: 0,
                                    )
                            )
                        }
    }
    ;

    abstract fun toItems(transactions: List<Transaction>?): List<TransactionsSummaryPieItem>

    fun List<Pair<String?, Int>>.addColours(): List<TransactionsSummaryPieItem> {
        if (isEmpty()) return emptyList()

        val nonNullCount = count { it.first != null }
        val increment = 1f / (nonNullCount - 1)
        val colors = List(nonNullCount) { it * increment }.shuffled().toMutableList()

        return map { (name, amount) ->
            val color = if (name == null) null else ColorUtils.asCategoryColor(colors.removeFirst())
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
