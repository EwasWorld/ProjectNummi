package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.utils.UniqueClassSet

data class ViewTransactionsState(
        val transactions: List<Transaction> = emptyList(),
        val manageItemDialogState: ManageItemDialogState<Transaction>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Transaction>? = null,
        val extras: UniqueClassSet<ViewTransactionsExtra> = UniqueClassSet(),
        val selectedTransactionId: Int? = null,
)
