package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState

data class ViewTransactionsState(
        val transactions: List<Transaction> = listOf(),
        val manageItemDialogState: ManageItemDialogState<Transaction>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Transaction>? = null,
        val editTransactionInitiatedFor: Transaction? = null,
)
