package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.database.transaction.DatabaseTransaction
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.utils.UniqueClassSet

data class ViewTransactionsState(
        /**
         * True when displaying recurring transactions, false for actual user data
         * @see [DatabaseTransaction.isRecurring]
         */
        val isRecurring: Boolean = false,
        val transactions: List<Transaction> = emptyList(),
        val manageItemDialogState: ManageItemDialogState<Transaction>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Transaction>? = null,
        val extras: UniqueClassSet<ViewTransactionsExtra> = UniqueClassSet(),
)
