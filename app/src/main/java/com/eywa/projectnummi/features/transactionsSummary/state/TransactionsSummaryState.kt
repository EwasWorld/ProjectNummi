package com.eywa.projectnummi.features.transactionsSummary.state

import com.eywa.projectnummi.database.transaction.TransactionsFilters
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryGrouping.CATEGORY
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.model.objects.Transaction

fun List<TransactionsSummaryPieItem>.getTotal() =
        sumOf { item ->
            item.amount.takeIf { it > 0 } ?: 0
        }.takeIf { it > 0 }

data class TransactionsSummaryState(
        val currentScreen: TransactionsSummaryTabSwitcherItem = TransactionsSummaryTabSwitcherItem.FILTERS,
        val transactions: List<Transaction>? = null,

        val accounts: List<Account>? = null,
        val categories: List<Category>? = null,
        val people: List<Person>? = null,

        val filtersState: TransactionsFilters = TransactionsFilters(),
        val outgoingIsPositive: Boolean = true,

        val currentGrouping: TransactionsSummaryGrouping = CATEGORY,
        val selectedItemIndex: Int? = null,
) {
    val selectedAccounts = get(filtersState.selectedAccountIds, accounts) { id, item -> item.id == id }
    val selectedCategories = get(filtersState.selectedCategoryIds, categories) { id, item -> item.id == id }
    val selectedPeople = get(filtersState.selectedPersonIds, people) { id, item -> item.id == id }

    fun <I> get(ids: List<Int?>, items: List<I>?, matches: (Int, I) -> Boolean) =
            items?.let {
                ids
                        .associateWith { id -> id?.let { items.find { matches(id, it) } } }
                        .filter { (key, value) -> key == null || value != null }
                        .values
                        .toList()
            } ?: emptyList()

    val groupedItems by lazy {
        currentGrouping
                .toItems(transactions)
                .applyAmountsSign()
                .sortedByDescending { it.amount }
                .addArcs()
    }
    val selectedItem by lazy { selectedItemIndex?.let { groupedItems.getOrNull(it) } }

    private fun List<TransactionsSummaryPieItem>.addArcs(): List<TransactionsSummaryPieItem> {
        val total = getTotal() ?: return emptyList()
        val percentage = 360f / total
        var currentAngle = 0f
        return map { item ->
            val startAngle = currentAngle
            val itemArc = (item.amount * percentage).takeIf { it > 0f } ?: return@map item
            currentAngle += itemArc
            item.copy(startAngleDegrees = startAngle, arcAngleDegrees = itemArc)
        }
    }

    private fun List<TransactionsSummaryPieItem>.applyAmountsSign() =
            takeIf { outgoingIsPositive } ?: map { it.copy(amount = it.amount * -1) }

    fun getSegmentIndex(thetaDegrees: Float) = groupedItems
            .takeWhile { (it.startAngleDegrees ?: 0f) < thetaDegrees.mod(360f) }
            .count { it.startAngleDegrees != null } - 1
}
