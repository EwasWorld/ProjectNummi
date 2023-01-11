package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.model.Transaction

data class ViewTransactionsState(
        val transactions: List<Transaction> = listOf(),
        val manageItemDialogState: ManageItemDialogState<Transaction>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Transaction>? = null,
        val editTransactionInitiatedFor: Transaction? = null,
)
