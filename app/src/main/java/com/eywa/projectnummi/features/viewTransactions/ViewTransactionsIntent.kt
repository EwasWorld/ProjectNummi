package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.model.Transaction

sealed class ViewTransactionsIntent {
    data class TransactionClicked(val transaction: Transaction) : ViewTransactionsIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ViewTransactionsIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ViewTransactionsIntent()
}
