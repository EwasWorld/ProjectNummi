package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ViewTransactionsExtra {
    data class ManageTabChanged(val tab: ManageTabSwitcherItem) : ViewTransactionsExtra()
    object AddTransactions : ViewTransactionsExtra()
    data class EditTransaction(val transaction: Transaction) : ViewTransactionsExtra()
    data class NewTransactionFromRecurring(val transaction: Transaction) : ViewTransactionsExtra() {
        init {
            require(transaction.isRecurring) { "Cannot create a new transaction from a non-recurring transaction" }
        }
    }
}
