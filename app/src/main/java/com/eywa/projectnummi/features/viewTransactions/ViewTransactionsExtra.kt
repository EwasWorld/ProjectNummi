package com.eywa.projectnummi.features.viewTransactions

import androidx.navigation.NavController
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.navigation.NummiNavArgument
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ViewTransactionsExtra {
    abstract fun handle(
            navController: NavController,
            viewModel: ViewTransactionsViewModelHelper,
    )

    data class ManageTabChanged(val tab: ManageTabSwitcherItem) : ViewTransactionsExtra() {
        override fun handle(
                navController: NavController,
                viewModel: ViewTransactionsViewModelHelper,
        ) {
            tab.navRoute.navigate(navController)
            viewModel.handleViewTransactionIntent(ViewTransactionsIntent.ClearExtra(this))
        }
    }

    object AddTransactions : ViewTransactionsExtra() {
        override fun handle(
                navController: NavController,
                viewModel: ViewTransactionsViewModelHelper,
        ) {
            NummiNavRoute.ADD_TRANSACTIONS.navigate(
                    navController,
                    mapOf(NummiNavArgument.TICK_SAVE_AS_RECURRING to viewModel.isRecurring.toString()),
            )
            viewModel.handleViewTransactionIntent(ViewTransactionsIntent.ClearExtra(this))
        }
    }

    data class EditTransaction(val transaction: Transaction) : ViewTransactionsExtra() {
        override fun handle(
                navController: NavController,
                viewModel: ViewTransactionsViewModelHelper,
        ) {
            NummiNavRoute.EDIT_TRANSACTIONS.navigate(
                    navController, mapOf(NummiNavArgument.TRANSACTION_ID to transaction.id.toString())
            )
            viewModel.handleViewTransactionIntent(ViewTransactionsIntent.ClearExtra(this))
        }
    }

    data class NewTransactionFromRecurring(val transaction: Transaction) : ViewTransactionsExtra() {
        init {
            require(transaction.isRecurring) { "Cannot create a new transaction from a non-recurring transaction" }
        }

        override fun handle(
                navController: NavController,
                viewModel: ViewTransactionsViewModelHelper,
        ) {
            NummiNavRoute.ADD_TRANSACTIONS_FROM_RECURRING.navigate(
                    navController,
                    mapOf(
                            NummiNavArgument.TRANSACTION_ID to transaction.id.toString(),
                            NummiNavArgument.INIT_FROM_RECURRING_TRANSACTION to true.toString(),
                    )
            )
            viewModel.handleViewTransactionIntent(ViewTransactionsIntent.ClearExtra(this))
        }
    }
}
